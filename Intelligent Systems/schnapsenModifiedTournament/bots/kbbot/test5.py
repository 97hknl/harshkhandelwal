import kb, sys
from kb import KB, Boolean, Integer, Constant

ACE = 'a'
PLAYED_ACE = 'pa'
a = [0] * 20
pa = [0] * 20

for i in range(20):
    a[i] = Boolean(ACE + str(i))
    pa[i] = Boolean(PLAYED_ACE + str(i))

kb = KB()

for i in range(0, 20, 5):
    kb.add_clause(a[i])

for i in range(0, 20, 5):
    kb.add_clause(~a[i], pa[i])
    kb.add_clause(a[i], ~pa[i])


#kb.add_clause(~pa[1])

# Print all models of the knowledge base
for model in kb.models():
    print model

# Print out whether the KB is satisfiable (if there are no models, it is not satisfiable)
print kb.satisfiable()
