package filter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterAndSorter<E> {
    private Comparator<E> comp;
    private List<Predicate<E>> pred;
    private List<E> list;

    public FilterAndSorter(List<E> list, List<Predicate<E>> pred, Comparator<E> comp) {
        this.list = list;
        this.pred = pred;
        this.comp = comp;
    }

    public List<E> doFilter() {
        List<E> filLst = list;

        for (Predicate<E> x : pred) {
            filLst = filLst.stream().filter(x).collect(Collectors.toList());
        }

        return filLst.stream().sorted(comp).collect(Collectors.toList());
    }
}
