package edu.berkeley.cs.amplab.vardiff;

import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BiFunction;

public class BimonotonicAStarSearcherTest {

  private static ArrayList<Integer> randomList(Random random, int n, int m) {
    ArrayList<Integer> list = new ArrayList<>(n);
    int sum = 0;
    for (int i = 0; i < n; ++i) {
      list.add(sum += random.nextInt(m));
    }
    return list;
  }

  @Test
  public void testSearch() {
    Random random = new Random();
    ArrayList<Integer>
        lhs = randomList(random, 1000, 4),
        rhs = randomList(random, 1000, 4);
    BiFunction<Integer, Integer, Integer> biFunction = (x, y) -> x + y;
    Multiset<Integer> multiset = HashMultiset.create();
    for (Integer i : lhs) {
      for (Integer j : rhs) {
        multiset.add(biFunction.apply(i, j));
      }
    }
    Integer previous = null;
    for (
        Iterator<Integer> iterator = BimonotonicAStarSearcher.<Integer, Integer, Integer>builder()
            .setBiFunction(biFunction)
            .setComparator(Comparator.naturalOrder())
            .build()
            .search(lhs, rhs)
            .iterator();
        iterator.hasNext();) {
      Integer next = iterator.next();
      if (null != previous) {
        assertTrue(previous.compareTo(next) <= 0);
      }
      assertTrue(multiset.remove(previous = next));
    }
    assertTrue(multiset.isEmpty());
  }
}
