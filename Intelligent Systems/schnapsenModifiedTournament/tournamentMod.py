#!usr/bin/env python
"""
A command line program for multiple games between several bots.

For all the options run
python play.py -h
"""

from argparse import ArgumentParser
from api import State, util, engine
import random

from bots.rdeepmodified.rdeepmodified import ratio_options


def matchSeed(index):
    return (index * index * 5 + 15) * 10 + index + 5

def run_tournament(options):

    # botnames = options.players.split
    # print botnames
    botnames = ["rdeep", "rdeepmodified"]
    bots = []
    for botname in botnames:
        bots.append(util.load_player(botname))

    n = len(bots)
    wins = [0] * len(bots)
    matches = [(p1, p2) for p1 in range(n) for p2 in range(n) if p1 < p2]

    totalgames = (n*n - n)/2 * options.repeats
    playedgames = 0

    print('Playing {} games:'.format(totalgames))
    for a, b in matches:
        for r in range(options.repeats):

            # if random.choice([True, False]):
            if int(options.first) == 1:
                p = [a, b]
            else:
                p = [b, a]

            # Generate a state with a random seed
            start = State.generate(id = matchSeed(r), phase=int(options.phase))

            winner = engine.play(bots[p[0]], bots[p[1]], start, options.max_time*1000, verbose=False)

            #TODO: ALSO IMPLEMENT POINTS FOR WINNING
            if winner is not None:
                winner = p[winner[0] - 1]
                wins[winner] += 1

            playedgames += 1
    print ratio_options
    print ('Starting: {}'.format('rdeep' if int(options.first) == 1 else 'rdeepmodified'))
    print('Wins: {:.0f}% / {}'.format(float(wins[1]) / (wins[0] + wins[1])* 100, playedgames))


if __name__ == "__main__":

    ## Parse the command line options
    parser = ArgumentParser()

    parser.add_argument("-s", "--starting-phase",
                        dest="phase",
                        help="Which phase the game should start at.",
                        default=1)

    parser.add_argument("-p", "--players",
                        dest="players",
                        help="Comma-separated list of player names (enclose with quotes).",
                        default="rand,bully,rdeep")

    parser.add_argument("-r", "--repeats",
                        dest="repeats",
                        help="How many matches to play for each pair of bots",
                        type=int, default=10)

    parser.add_argument("-t", "--max-time",
                        dest="max_time",
                        help="maximum amount of time allowed per turn in seconds (default: 5)",
                        type=int, default=5)

    parser.add_argument("-first", "--first",
                        dest="first",
                        help="Who starts in all the games",
                        type=int, default=5)

    options = parser.parse_args()

    run_tournament(options)