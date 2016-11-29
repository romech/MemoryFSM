package fsm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
/**
 * FSM objects only store data of their state
 */
class FSM
{
    int pos;
    char q;
    LinkedList<Character> st;
    
    private FSM(){};
    public FSM(char q)
    {
        pos = 0;
        this.q = q;
        st = new LinkedList<>();
        st.addLast('Z');
    }
    /**
     * A copying and modifying constructor
     * @param act - пара pY
     * @param nextPos true when symbol is read, false when epsilon
     */
    public FSM(FSM orig, String act, boolean nextPos)
    {
        pos = nextPos ? orig.pos + 1 : orig.pos;
        q = act.charAt(0);
        st = (LinkedList<Character>)orig.st.clone();
        st.removeLast();
        for (int i = act.length()-1; i > 0; i--)
            if (act.charAt(i) != 'e')
                st.addLast(act.charAt(i));
    }
}
/**
 * Main class for parsing strings
 */
public class GrammarRecognizer {  
    static HashMap<String, HashSet<String>> rules = new HashMap<>();
    static HashMap<Character, String> errors = new HashMap<>();
    static HashSet<String> empty_def = new HashSet<>(0);
    static char init;
    static String finite, inp;
    static HashSet<String> delta (char q, char symb, char st_top)
    {
        return (HashSet<String>)rules.getOrDefault(
                String.valueOf(q) + symb + st_top, empty_def);
    }
    static boolean inEndState(FSM mach)
    {
        return mach.pos == inp.length() && finite.indexOf(mach.q) >= 0;
    }
    /**
     * First assign an input string to inp, then call this function
     */
    static void recognise()
    {
        LinkedList<FSM> queue = new LinkedList<>();
        FSM cur = new FSM(init);
        while(!inEndState(cur))
        {
            HashSet<String> acts = delta(cur.q, 'e', cur.st.getLast());
            for (String act : acts)
                queue.add(new FSM(cur, act, false));
            if (cur.pos < inp.length())
            {
                acts = delta(cur.q, inp.charAt(cur.pos), cur.st.getLast());
                for (String act : acts)
                    queue.add(new FSM(cur, act, true));
            }
            if (queue.isEmpty())
                break;
            else
                cur = queue.pop();
        }
        System.out.println(inEndState(cur) ? "Accepted" :
                (errors.getOrDefault(cur.q, "Error") + " at position "
                                        + String.valueOf(cur.pos+1)));
    }
    public static void main(String[] args) {
        try
        {
            Scanner scn = new Scanner(new File("fsm.txt"));
            //README on https://github.com/romech/MemoryFSM
            init = scn.next().charAt(0);
            finite = scn.next();
            scn.nextLine();
            for (String err = scn.nextLine(); err.charAt(0)!='-';
                                            err = scn.nextLine())
                errors.put(err.charAt(0), err.substring(2));
            while(scn.hasNext())
            {
                String delta_arg = scn.next();
                if (!rules.containsKey(delta_arg))
                    rules.put(delta_arg, new HashSet<>(2, 1));
                ((HashSet<String>)rules.get(delta_arg)).add(scn.next());
            }
            scn.close();
            scn = new Scanner(System.in);
            inp = scn.nextLine();
            recognise();
        } catch (IOException exc) {
            System.err.println("Error reading file: " + exc.getMessage());
        }
    }
}