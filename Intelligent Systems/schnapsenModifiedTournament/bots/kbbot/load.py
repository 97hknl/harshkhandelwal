from kb import KB, Boolean, Integer

JACKS = 'j'
PLAYED_JACKS = 'pj'
j = [0] * 20
pj = [0] * 20

QUEENS = 'q'
PLAYED_QUEENS = 'pq'
q = [0] * 20
pq = [0] * 20

KINGS = 'k'
PLAYED_KINGS = 'pk'
k = [0] * 20
pk = [0] * 20

for i in range(20):
    j[i], pj[i] = Boolean(JACKS + str(i)), Boolean(PLAYED_JACKS + str(i))
    q[i], pq[i] = Boolean(QUEENS + str(i)), Boolean(PLAYED_QUEENS + str(i))
    k[i], pk[i] = Boolean(KINGS + str(i)), Boolean(PLAYED_KINGS + str(i))


def general_information(kb):
    for i in range(0, 20, 5):
        kb.add_clause(j[i + 4])
    for i in range(0, 20, 5):
        kb.add_clause(q[i + 3])
    for i in range(0, 20, 5):
        kb.add_clause(k[i + 2])

def strategy_knowledge(kb):
    for i in range(0, 20, 5):
        kb.add_clause(~j[i + 4], pj[i + 4])
        kb.add_clause(j[i + 4], ~pj[i + 4])

    for i in range(0, 20, 5):
        kb.add_clause(~q[i + 3], pq[i + 3])
        kb.add_clause(q[i + 3], ~pq[i + 3])

    for i in range(0, 20, 5):
        kb.add_clause(~k[i + 2], pk[i + 2])
        kb.add_clause(k[i + 2], ~pk[i + 2])
