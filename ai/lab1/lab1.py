import os
import sys
from anytree import Node, RenderTree
from copy import deepcopy


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


def populate(all3, tree):
    if len(all3) != 0:
        letter = all3[-1]
        for i in range(0, 10):
            if i not in tree.nod.values():
                kid = Tree()
                kid.nod = copy.deepcopy(tree.nod)
                kid.nod[letter] = i
                all3 = all3.replace(letter, "")
                populate(all3, kid)
                tree.kids.append(kid)


def file_open():
    try:
        f = open(sys.argv[1], "r")
        return f
    except FileNotFoundError:
        print("Fisierul de input nu exista.")
        exit(1)


def read_matrix(f, n):
    sudoku = []
    for line in f:
        spl_line = line.split(" ")
        spl_line = [int(x) for x in spl_line]
        sudoku.append(spl_line)

    return sudoku


def main():
    if len(sys.argv) != 2:
        print("Error:", sys.argv[0], '<fisier de input>')
        return
    f = file_open()

    n = int(f.readline())
    a = read_matrix(f, n)

    tree = Tree()
    # tree = Node(deepcopy(a))
    # parent = tree

    populate(tree, matrix)


if __name__ == '__main__':
    main()