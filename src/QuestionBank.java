import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static repository of all quiz questions.
 * Provides a single method to retrieve all questions grouped by theme.
 * 
 * @author GasTheJuice
 */
public class QuestionBank {
    
    /**
     * Returns a complete list of all quiz questions.
     * Questions are hard-coded and grouped by theme.
     * 
     * @return unmodifiable list of all questions
     */
    public static List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();

        String theme1 = "ifs loops and operators";
        list.add(new Question(theme1,
            "What is the output of:\nint a=0;\nif (a) {\n printf(\"yes\");\n} else {\n printf(\"no\");\n}",
            Question.QuestionType.RADIO,
            Arrays.asList("yes","no","error"),
            Arrays.asList("no"),
            "Remember that booleans are secretly integer types."));

        list.add(new Question(theme1,
            "Which of the following are valid loop types in C? (choose all that apply)",
            Question.QuestionType.CHECKBOX,
            Arrays.asList("for", "for each", "while", "do while"),
            Arrays.asList("for", "while", "do while"),
            "C does not have a for each loop like some higher level languages."));

        list.add(new Question(theme1,
            "What does the ternary expression (x > 0 ? x : -x) compute?",
            Question.QuestionType.TEXT,
            null,
            Arrays.asList("absolute value", "abs", "absolute"),
            "Absolute value."));

        list.add(new Question(theme1,
            "Given:\nint i=0;\nwhile(++i < 3){\n printf(\"%d \", i);\n}\nwhat prints?",
            Question.QuestionType.RADIO,
            Arrays.asList("0 1 2", "1 2", "1 2 3", "2 3", "0 1 2 3", "error"),
            Arrays.asList("1 2"),
            "++i increments i before evaluating the expression as opposed to i++."));

        list.add(new Question(theme1,
            "Which expression always evaluates to true if the first bit of x is always 1? (choose single)",
            Question.QuestionType.RADIO,
            Arrays.asList("(x & 1) != 1", "x == 1", "x == 0", "(x ^ 1) == 1", "(x | 1) != 0"),
            Arrays.asList("(x | 1) != 0"),
            "This text is too short to fully explain bitwise operators so look it up."));

        list.add(new Question(theme1,
            "If you evaluate a complex boolean expression and store the boolean result in a variable, then pass that variable to an if — why might you do that?",
            Question.QuestionType.TEXT,
            null,
            Arrays.asList("for clarity", "reuse result", "to avoid recomputing", "to reuse result", "for simplicity", "for reusability", "to reuse result", "to avoid recomputation", "avoid recomputing", "for clarity and reusability"),
            "For clarity and reusability."));

        list.add(new Question(theme1,
            "How many times will /*code*/ run?\nint i=1;\ndo { /*code*/ } while(i--!=0);",
            Question.QuestionType.NUMERIC,
            null,
            Arrays.asList("2"),
            "The do while loop first runs the code, then checks the result and i-- decrements i only after the expression has been evaluated."));

        list.add(new Question(theme1,
            "Click the bug (once clicked press next, clicking again will overwrite your first answer).",
            Question.QuestionType.IMAGE_CLICK,
            null,
            null,
            "Look closely at the condition iside the if statement.",
            "images/bitwise_instead_of_logical.png",
            List.of(new Rectangle(65, 121, 57, 17))));

        list.add(new Question(theme1,
            "Click the bug (once clicked press next, clicking again will overwrite your first answer).",
            Question.QuestionType.IMAGE_CLICK,
            null,
            null,
            "Look closely at the condition in the loop.",
            "images/no_incrementation.png",
            List.of(new Rectangle(96, 98, 70, 18))));
        
        list.add(new Question(theme1,
            "How many times does printf execute?\nfor(int i=0; i<5; i++) {\n  if(i++ < 3) printf(\"*\");\n}",
            Question.QuestionType.SLIDER,
            Arrays.asList("0", "6", "3"),
            Arrays.asList("2"),
            "i++ in condition uses old value, then increments."));
        
        list.add(new Question(theme1,
            "What is x after: int x = -8; x >>= 1; // Assume 32-bit int",
            Question.QuestionType.SLIDER,
            Arrays.asList("-16", "-4", "-8"),
            Arrays.asList("-4"),
            "Right shift on negative (arithmetic shift) fills with 1s."));
        
        list.add(new Question(theme1,
            "Given:\nint x = 5 + 3 * 2 >> 1\nwhat is the value of x?",
            Question.QuestionType.COMBOBOX,
            Arrays.asList("1", "2", "3", "5", "6", "8", "11", "error"),
            Arrays.asList("5"),
            "Bitwise operators are always applied last."));

        String theme2 = "strings arrays and pointers";
        list.add(new Question(theme2,
            "How is a C string stored in memory?",
            Question.QuestionType.RADIO,
            Arrays.asList("Array of chars terminated by '\\0'", "Linked list of chars", "Pointer with length field", "UTF-8 object", "Strings are a primitive data type"),
            Arrays.asList("Array of chars terminated by '\\0'"),
            "Strings are made up of characters in a data structure."));

        list.add(new Question(theme2,
            "Which of these operations are valid on char *s when s points to a C string? (choose all)",
            Question.QuestionType.CHECKBOX,
            Arrays.asList("s[0]", "s+\"String\"", "strlen(s)", "s++", "s->len"),
            Arrays.asList("s[0]", "strlen(s)", "s++"),
            "+ is not a valid concatenation operator in C, -> is used to point to a member of a struct."));

        list.add(new Question(theme2,
            "How many bytes does int arr[6][7] occupy?",
            Question.QuestionType.NUMERIC,
            null,
            Arrays.asList("168"),
            "A standard integer is 4 bytes."));

        list.add(new Question(theme2,
            "Given char s[] = \"abc\";, what is sizeof(s)?",
            Question.QuestionType.NUMERIC,
            null,
            Arrays.asList("4"),
            "Don't forget about '\\0'."));

        list.add(new Question(theme2,
            "What is the main difference between array and pointer parameters in function signatures?",
            Question.QuestionType.TEXT,
            null,
            Arrays.asList("arrays decay to pointer to first element", "array parameter is pointer", "size not passed", "arrays decay to pointers", "they are the same", "there is no difference"),
            "Arrays decay to pointers."));

        list.add(new Question(theme2,
            "Which statement about pointer arithmetic is true? (choose single)",
            Question.QuestionType.RADIO,
            Arrays.asList("p++ moves by one byte","p++ moves by sizeof(*p) bytes","p++ sets pointer to next bit","p++ is invalid"),
            Arrays.asList("p++ moves by sizeof(*p) bytes"),
            "Moving by 1 byte or 1 bit would lead to undefined behaviour for larger types."));

        list.add(new Question(theme2,
            "Which memory layout is correct for a 2D array int a[2][3] in C? (choose single)",
            Question.QuestionType.RADIO,
            Arrays.asList("Column-major contiguous", "Array of pointers to rows", "Row-major contiguous", "Interleaved"),
            Arrays.asList("Row-major contiguous"),
            "Arrays are contiguous."));

        list.add(new Question(theme2,
            "Click the bug (once clicked press next, clicking again will overwrite your first answer).",
            Question.QuestionType.IMAGE_CLICK,
            null,
            null,
            "Look closely at the way the string is initialized.",
            "images/missing_terminator.png",
            List.of(new Rectangle(165, 61, 174, 21))));

        list.add(new Question(theme2,
            "Click the bug (once clicked press next, clicking again will overwrite your first answer).",
            Question.QuestionType.IMAGE_CLICK,
            null,
            null,
            "Look closely at the for loop condition.",
            "images/index_out_of_bounds.png",
            List.of(new Rectangle(75, 103, 202, 19))));
        
        list.add(new Question(theme2,
            "int arr[3][4]; &arr[1][2] - &arr[0][1] equals? (in elements)",
            Question.QuestionType.SLIDER,
            Arrays.asList("0", "6", "3"),
            Arrays.asList("5"),
            "Row-major!"));
        
        list.add(new Question(theme2,
            "char *p = \"hello\"; sizeof(p) - sizeof(\"hello\"); gives?",
            Question.QuestionType.SLIDER,
            Arrays.asList("0", "8", "4"),
            Arrays.asList("2"),
            "Pointer size sizeof(p) returns the size of the pointer itself, not what it points to."));
        
        list.add(new Question(theme2,
            "char *s1 = \"abc\", *s2 = \"abc\"; s1[1] = 'x'; s2[1] == ?",
            Question.QuestionType.COMBOBOX,
            Arrays.asList("'b'", "'x'", "undefined"),
            Arrays.asList("undefined"),
            "String literals are read-only; modifying = UB."));

        String theme3 = "functions structs and memory";
        list.add(new Question(theme3,
            "What happens when you pass an int to a function in C?",
            Question.QuestionType.RADIO,
            Arrays.asList("reference passed", "pointer auto created", "value copied (pass-by-value)", "global changed"),
            Arrays.asList("value copied (pass-by-value)"),
            "There are no pointers involved."));

        list.add(new Question(theme3,
            "How do you allocate an array of n ints at runtime?",
            Question.QuestionType.TEXT,
            null,
            Arrays.asList("malloc(n * sizeof(int))", "malloc(n * 4)"),
            "Allocating at runtime means the size is not known at compilation time, therefore the size depends on some outside factor, so we use malloc()."));

        list.add(new Question(theme3,
            "Which functions must be used to free and resize memory allocated by malloc?",
            Question.QuestionType.CHECKBOX,
            Arrays.asList("delete()", "resize()", "realloc()", "free()", "freeMemory()"),
            Arrays.asList("free()", "realloc()"),
            null));

        list.add(new Question(theme3,
            "If you write to *ptr after free(ptr), what is this error called?",
            Question.QuestionType.RADIO,
            Arrays.asList("use-after-free", "double-free", "memory-leak", "buffer-overflow"),
            Arrays.asList("use-after-free"),
            null));

        list.add(new Question(theme3,
            "How do you declare a struct type 'Point' with two ints x and y? (text)",
            Question.QuestionType.TEXT,
            null,
            Arrays.asList(
                "typedef struct { int x; int y; } Point;",
                "struct Point { int x; int y; }; typedef struct Point Point;",
                "typedef struct { int x, y; } Point;",
                "struct Point { int x, y; }; typedef struct Point Point;",
                "typedef struct {int x; int y;} Point;",
                "struct Point {int x; int y;}; typedef struct Point Point;",
                "typedef struct {int x, y;} Point;",
                "struct Point {int x, y;}; typedef struct Point Point;"
            ),
            "Use typedef on a struct."));

        list.add(new Question(theme3,
            "True/False: enums in C are guaranteed to be 4 bytes on all platforms.",
            Question.QuestionType.RADIO,
            Arrays.asList("True", "False"),
            Arrays.asList("False"),
            "Enums are just integer constants."));

        list.add(new Question(theme3,
            "When should you use free() in relation to malloc()?",
            Question.QuestionType.TEXT,
            null,
            Arrays.asList("when you no longer need the allocated memory", "after memory use to avoid leaks", "when memory is no longer needed", "when allocated memory is no longer needed"),
            null));

        list.add(new Question(theme3,
            "Click the bug (once clicked press next, clicking again will overwrite your first answer).",
            Question.QuestionType.IMAGE_CLICK,
            null,
            null,
            "Look at where the greet() function is declared.",
            "images/missing_declaration.png",
            List.of(new Rectangle(47, 64, 77, 19))));

        list.add(new Question(theme3,
            "Click the bug (once clicked press next, clicking again will overwrite your first answer).",
            Question.QuestionType.IMAGE_CLICK,
            null,
            null,
            "The function returns the address of a local variable.",
            "images/dangling_pointer.png",
            List.of(new Rectangle(42, 102, 87, 17))));
        
        list.add(new Question(theme3,
            "struct { char c; int i; char d; } s; // sizeof(s) with 4-byte alignment?",
            Question.QuestionType.SLIDER,
            Arrays.asList("6", "12", "8"),
            Arrays.asList("12"),
            "Structs add padding based on the size of the biggest member."));
        
        list.add(new Question(theme3,
            "struct { unsigned int a:3; unsigned int b:5; unsigned int c:8; } s; // sizeof(s) on 32-bit system?",
            Question.QuestionType.SLIDER,
            Arrays.asList("0", "4", "2"),
            Arrays.asList("4"),
            "Bitfields are packed into storage units (int=4 bytes); 3+5+8=16 bits → 4 bytes total."));
        
        list.add(new Question(theme3,
            "What is the correct syntax for a function pointer to int func(int)?",
            Question.QuestionType.COMBOBOX,
            Arrays.asList("int *f(int)", "(*int f)(int)", "int (*f)(int)", "int f(*int)"),
            Arrays.asList("int (*f)(int)"),
            "Read right-to-left: pointer to function returning int."));

        return list;
    }
}