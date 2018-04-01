import copy
import queue

class Tree:
    nod = 0
    kids = 0

    def __init__(self):
        self.nod = dict()
        self.kids = list()

    def add(self, kid):
        self.kids.append(kid)

    def get_kids(self):
        return self.kids


oper = "+"
# op1 = "A"
# op2 = "B"
# rez = "C"
op1 = "SEND"
op2 = "MORE"
rez = "MONEY"


def populate(all3, tree):
    if len(all3) != 0:
        letter = all3[-1]
        for i in range(0,10):
            if i not in tree.nod.values():
                kid = Tree()
                kid.nod = copy.deepcopy(tree.nod)
                kid.nod[letter] = i
                all3 = all3.replace(letter, "")
                populate(all3, kid)
                tree.kids.append(kid)


def is_valid(node):
    op11 = op1
    op22 = op2
    rez2 = rez
    for key in node.nod:
        node.nod[key] = str(node.nod[key])

    op11 = ''.join(node.nod.get(ch, ch) for ch in op11)
    op22 = ''.join(node.nod.get(ch, ch) for ch in op22)
    rez2 = ''.join(node.nod.get(ch, ch) for ch in rez2)

    if op11[0] == "0" or op22[0] == "0" or rez2[0] == "0":
        return False

    # print(op11)
    # print(op22)
    # print(rez2)
    # print()

    if oper == "+":
        try:
            if int(op11) + int(op22) == int(rez2):
                # print("true")
                return True
        except ValueError as v:
            pass
        return False
    elif oper == "-":
        try:
            if int(op11) - int(op22) == int(rez2):
                    # print("true")
                    return True
        except ValueError as v:
            pass
        return False


def dfs(root):
    nodes = []
    stack = [root]
    while stack:
        current_node = stack[0]
        stack = stack[1:]
        if is_valid(current_node):
            nodes.append(current_node)
        for kid in current_node.get_kids():
            stack.insert(0, kid)
    return nodes


def apply_heuristics(node):
    op11 = op1
    op22 = op2
    rez2 = rez
    for key in node.nod:
        node.nod[key] = str(node.nod[key])

    op11 = ''.join(node.nod.get(ch, ch) for ch in op11)
    op22 = ''.join(node.nod.get(ch, ch) for ch in op22)
    rez2 = ''.join(node.nod.get(ch, ch) for ch in rez2)

    diferente = 0
    minim = min([len(op11), len(op22)])
    for i in range(1,minim):
        dif = int(rez2[-i]) - (int(op11[-i]) + int(op22[-i]))
        diferente += dif

    return diferente


def gbfs(root):
    pq = queue.PriorityQueue()
    pq.put((0, root))
    while not pq.empty():
        current_node = pq.get()[1]
        if is_valid(current_node):
            return current_node
        for kid in current_node.get_kids():
            priority = apply_heuristics(kid)
            pq.put((priority, kid))

    return None


if __name__ == "__main__":
    all3 = op1 + op2 + rez
    tree = Tree()

    for letter in all3:
        tree.nod[letter] = 9
    populate(all3, tree)

    # lista = dfs(tree)
    # for l in lista:
    #     print(l.nod)

    sol = gbfs(tree)
    print(sol.nod)




