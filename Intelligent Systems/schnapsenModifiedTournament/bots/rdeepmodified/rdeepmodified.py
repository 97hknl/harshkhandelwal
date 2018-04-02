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
from math import sqrt
import json
from bots.alphabeta import alphabeta

ratio_options = {"ratio_weight": 6, "marriage_weight": [2, 2], "hand_weight": [0, 0, 0], "trump_weight": [2, 2, 2]}
ratio_options = {"ratio_weight": 6, "marriage_weight": [2, 2], "hand_weight": [0, 0, 0], "trump_weight": [2, 2, 2]}

class Bot:

	# How many samples to take per move
	__num_samples = -1
	# How deep to sample
	__depth = -1
	__ratio_options = ratio_options
	#__alphabeta = alphabeta.Bot()

	def __init__(self, num_samples=4, depth=8):
		self.__num_samples = num_samples
		self.__depth = depth

	def get_move(self, state):

		# See if we're player 1 or 2
		player = state.whose_turn()
		#if state.get_phase() == 2:
			#return self.__alphabeta.get_move(state)
		# Get a list of all legal moves
		moves = state.moves()

		# Sometimes many moves have the same, highest score, and we'd like the bot to pick a random one.
		# Shuffling the list of moves ensures that.
		random.shuffle(moves)

		best_score = float("-inf")
		best_move = None

		scores = [0.0] * len(moves)
		for move in moves:
			if(move[1] is not None and move[0] is None):
				return move
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

			score += self.combo_heuristic(st, player)

		return score/float(self.__num_samples)

	def default_heuristic(self, state, player):
		points = state.get_points(player)
		if points < 33:
			formula = points/33
			return ((formula * 2) + util.ratio_points(state, player))/3
		return util.ratio_points(state, player)

	def bad_heuristic(self, state):
		me = state.whose_turn()
		other = util.other(me)
		other_points = state.get_points(other)
		me_points = state.get_points(me)
		value =  1 - ((66 - me_points)/66)
		return value if value < 1 else 1

	def combo_heuristic(self, state, player):
		ratio_weight = self.__ratio_options["ratio_weight"]
		marriage_weight =self.__ratio_options["marriage_weight"][1]
		hand_weight = self.__ratio_options["hand_weight"][1]
		trump_weight = self.__ratio_options["trump_weight"][0]

		ratio_heuristic = self.default_heuristic(state, player)
		marriage_heuristic = self.marriage_heuristic(state, 1)
		# hand_heuristic = sqrt(self.hand_heuristic(state, player, 1) * self.bridge_hand_heuristic(state, player, 1))
		trump_heuristic = self.trump_heuristic(state, player, 1)
		hand_heuristic = self.advantage_high_card_ratio(state, player , 1)
		# trump_heuristic = self.advantage_trump_ratio(state, player , 1)
		if marriage_heuristic == 0:
			marriage_weight = self.__ratio_options["marriage_weight"][0]
		elif trump_heuristic > 0.6:
			trump_weight = self.__ratio_options["trump_weight"][2]
		elif trump_heuristic > 0.3:
			trump_weight = self.__ratio_options["trump_weight"][1]
		if hand_heuristic < 0.5:
			hand_weight = self.__ratio_options["hand_weight"][0]
		elif hand_heuristic > 0.7:
			hand_weight = self.__ratio_options["hand_weight"][2]

		result = ratio_heuristic * ratio_weight + marriage_heuristic * marriage_weight + hand_heuristic * hand_weight + trump_heuristic * trump_weight
		sum_weights = ratio_weight + marriage_weight + hand_weight + trump_weight
		#return sqrt(self.default_heuristic(state, player) * self.marriage_heuristic(state, marriage_weight))
		return result / sum_weights

	def advantage_high_card_ratio(self, state, player, weight):
		perspective = state.get_perspective(player)
		index = 0
		unseen_high_cards = 0;
		player_high_cards = 0

		for card in perspective:
			if index % 5 == 0 or index % 5 == 1:
				if card == "U":
					unseen_high_cards += 1
				elif card == ("P" + str(player) + "H"):
					player_high_cards += 1
			index = index + 1
		if (unseen_high_cards + player_high_cards) == 0:
			return 0.01
		return weight * float(player_high_cards) / (unseen_high_cards + player_high_cards)

	def advantage_trump_ratio(self, state, player, weight):
		perspective = state.get_perspective(player)
		index = 0
		unseen_trumps = 0
		player_trumps = 0
		trump_suit = ["C", "D", "H", "S"].index(state.get_trump_suit())
		for i in range(5*trump_suit, 5*(trump_suit+1)):
			if perspective[i] == "U":
				unseen_trumps += 1
			elif perspective[i] == ("P" + str(player) + "H"):
				player_trumps += 1
		if unseen_trumps + player_trumps == 0:
			return 0.01
		return weight * float(player_trumps) / (unseen_trumps + player_trumps)

	def hand_heuristic(self, state, player, hand_weight):
		score = [11, 10, 4, 3, 2]
		#score = [4, 3, 1, 0, 0]
		points = 0
		moves = state.moves()
		for move in moves:
			if move[0] is not None and move[1] is None:
				points = points + score[move[0] % 5]
		#hand_cards = state.hand()
		#for card in hand_cards:
			#points = points + score[card % 5]
		return hand_weight * (points/54)

	def bridge_hand_heuristic(self, state, player, hand_weight):
		score = [11, 10, 4, 3, 2]
		score = [4, 3, 1, 0, 0]
		points = 0
		moves = state.moves()
		for move in moves:
			if move[0] is not None and move[1] is None:
				points = points + score[move[0] % 5]
		#hand_cards = state.hand()
		#for card in hand_cards:
			#points = points + score[card % 5]
		value = hand_weight * (points/14)
		return min(1, value)

	def trump_heuristic(self, state, player, trump_weight):
		moves = state.moves()
		num_trumps = 0
		for move in moves:
			if move[0] is not None and move[1] is None and Deck.get_suit(move[0]) == state.get_trump_suit():
				num_trumps = num_trumps + 1

		hand_cards = state.hand()
		for card in hand_cards:
			if Deck.get_suit(card) == state.get_trump_suit():
				num_trumps = num_trumps + 1

		return trump_weight * (num_trumps / 12)

	def marriage_heuristic(self, state, marriage_weight):
		moves = state.moves()
		num_marriages = 0
		for move in moves:
			if move[0] is not None and move[1] is not None:
				num_marriages = num_marriages + 2 if Deck.get_suit(move[1]) == state.get_trump_suit() else num_marriages + 1
		marriage_points = num_marriages * 20
		return marriage_weight * (marriage_points * 2 / 60)
