import java.math.*;
import java.util.*;
import java.util.regex.*;

import com.google.common.collect.*;

// e.g., foo9 < foo10 < foo99 < foo100
public class HumanOrdering extends Ordering<String> {
  static Pattern p = Pattern.compile("[0-9]+|[^0-9]+");
  static List<Object> list(String input) {
    List<Object> list = Lists.newArrayList();
    Matcher m = p.matcher(input);
    while (m.find()) {
      String atom = m.group(0);
      if (atom.matches("[0-9]+"))
        list.add(new BigInteger(atom));
      else
        list.add(atom);
    }
    return list;
  }
  @Override
  public int compare(String left, String right) {
    List<Object> leftList = list(left);
    List<Object> rightList = list(right);
    return Ordering.from(new Comparator<Object>() {
      @Override
      public int compare(Object lhs, Object rhs) {
        if (lhs instanceof String) {
          if (rhs instanceof String)
            return ((String) lhs).compareTo((String) rhs);
          if (rhs instanceof BigInteger)
            return "a".compareTo("1");
        }
        if (lhs instanceof BigInteger) {
          if (rhs instanceof String)
            return "1".compareTo("a");
          if (rhs instanceof BigInteger)
            return ((BigInteger) lhs).compareTo((BigInteger) rhs);
        }
        throw new RuntimeException();
      }
    }).lexicographical().compare(leftList, rightList);
  }
}
