# MemoryFSM
Small console program that emulates finite state machine with memory to parse character sequences

To work with it you need to write FSM rules formally in a text file called "fsm.txt", put it in the folder with jar file.
Then run jar from console and write a line of characters. That's all.

Format of "fsm.txt":
1st line - an initial state, space, final states. Every state is marked with a single character. So this line may look like "a xyz".
Next lines are error names. Every error is associated with a FSM state. And if your FSM rejects a character sequence, then the last working FSM thread may tell the error. Every line must look so: "q Unexpected symbol".
Next line consists of only a dash: "-"
All following lines describe a main function of FSM: delta(q,a,X)->(p,Y). q is for a state, a - is a symbol from input (a=='e' <=> epsilon transition), X - a symbol from top of the stack, p - state to switch to, Y -  sequence of symbols to be placed on stack instead of X (may be empty; symbols are placed from last to first; 'e' again is for epsilon and  isn't placed on stack). Keep in mind that delta(q,a,X) may have several results, each should be placed on a separate line.

Attached "fsm.txt" describes an example FSM that accept sequences of letters x, y and z that equal "x^(3n)y^(2n)z^(3m)", n,m>=0.
