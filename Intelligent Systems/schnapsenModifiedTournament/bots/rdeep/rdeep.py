"""
RdeepBot - This bot looks ahead by following a random path down the game tree. That is,
 it assumes that all players have the same strategy as rand.py, and samples N random
 games following from a given move. It then ranks the moves by averaging the heuristics
 of the resulting states.
"""

# Import the API objects
from api import State, util
import random
from api import Deck


class Bot:

	# How many samples to take per move
	__num_samples = -1
	# How deep to sample
	__depth = -1

	def __init__(self, num_samples=4, depth=8):
		self.__num_samples = num_samples
		self.__depth = depth

	def get_move(self, state):

		# See if we're player 1 or 2
		player = state.whose_turn()

		# Get a list of all legal moves
		moves = state.moves()

		# Sometimes many moves have the same, highest score, and we'd like the bot to pick a random one.
		# Shuffling the list of moves ensures that.
		random.shuffle(moves)

		best_score = float("-inf")
		best_move = None

		scores = [0.0] * len(moves)

		for move in moves:
			for s in range(self.__num_samples):

				# If we are in an imperfect information state, make an assumption.

				sample_state = state.make_assumption() if state.get_phase() == 1 else state

				score = self.evaluate(sample_state.next(move), player)

				if score > best_score:
					best_score = score
					best_move = move

		return best_move # Return the best scoring move

	def evaluate(self,
				 state,     # type: State
				 player     # type: int
			):
		# type: () -> float
		"""
		Evaluates the value of the given state for the given player
		:param state: The state to evaluate
		:param player: The player for whom to evaluate this state (1 or 2)
		:return: A float representing the value of this state for the given player. The higher the value, the better the
			state is for the player.
		"""

		score = 0.0

		for _ in range(self.__num_samples):

			st = state.clone()

			# Do some random moves
			for i in range(self.__depth):
				if st.finished():
					break

				st = st.next(random.choice(st.moves()))

			score += self.default_heuristic(st, player)

		return score/float(self.__num_samples)

	def default_heuristic(self, state, player):
		return util.ratio_points(state, player)

	def bad_heuristic(self, state):
		me = state.whose_turn()
		other = util.other(me)
		other_points = state.get_points(other)
		me_points = state.get_points(me)
		value =  1 - ((66 - me_points)/66)
		return value if value < 1 else 1

	def combo_heuristic(self, state, player):
		ratio_weight = 0.8
		marriage_weight = 0.1
		hand_weight = 0.05
		trump_weight = 0.05

		ratio_heuristic = ratio_weight * self.default_heuristic(state, player)
		return ratio_heuristic + self.hand_heuristic(state, player, hand_weight) + self.trump_heuristic(state, player, trump_weight) + self.marriage_heuristic(state, marriage_weight)


	def hand_heuristic(self, state, player, hand_weight):
		score = [11, 10, 4, 3, 2]
		points = 0
		hand_cards = state.hand()
		for card in hand_cards:
			points = points + score[card % 5]

		return hand_weight * (1 - points/54)

	def trump_heuristic(self, state, player, trump_weight):
		moves = state.moves()
		num_trumps = 0
		for move in moves:
			if move[0] is not None and Deck.get_suit(move[0]) == state.get_trump_suit():
				num_trumps = num_trumps + 1

		hand_cards = state.hand()
		for card in hand_cards:
			if Deck.get_suit(card) == state.get_trump_suit():
				num_trumps = num_trumps + 1

		return trump_weight * (num_trumps / 11)

	def marriage_heuristic(self, state, marriage_weight):
		moves = state.moves()
		num_marriages = 0
		for move in moves:
			if move[0] is not None and move[1] is not None:
				num_marriages = num_marriages + 2 if Deck.get_suit(move[1]) == state.get_trump_suit() else num_marriages + 1
		marriage_points = num_marriages * 20
		return marriage_weight * (marriage_points / 60)