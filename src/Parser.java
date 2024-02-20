import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parser {

    private static final String WHITESPACE_REGEX = "\\s+";
    private static final String COMMENT_REGEX = "//.*";
    private static final Map<String, Integer> NUMBER = Map.of(
        "\\d+(?:\\.\\d+)?", 0
    ); 
    private static final Map<String, Integer> IDENTIFIER = Map.of(
        
    "[a-zA-Z][a-zA-Z0-9_]*", 0
    
    );
    private static final Map<String, Integer> DIRECTIONS = Map.of(
        "north", 0,
        "south", 1,
        "east", 2,
        "west", 3
    );

    private static final Map<String, Integer> COMMANDS = Map.of(
        "move", 0,
        "turn", 1,
        "face", 2,
        "put", 3,
        "pick", 4,
        "move-dir", 5,
        "run-dirs", 6,
        "move-face", 7,
        "null", 8
    );

    private static final Map<String, Integer> CONDITIONS = Map.of(
        "facing?", 0,
        "blocked?", 1,
        "can-put?", 2,
        "can-pick?", 3,
        "can-move?", 4,
        "isZero?", 5,
        "not", 6
    );

    private final BufferedReader reader;
    private final List<String> tokens;
    private int currentToken;

    public Parser(File file) throws IOException {
        reader = new BufferedReader(new FileReader(file));
        tokens = new ArrayList<>();
        tokenize();
    }

    private void tokenize() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll(COMMENT_REGEX, "");
            String[] split = line.split(WHITESPACE_REGEX);
            for (String token : split) {
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
            }
        }
        reader.close();
    }

    public boolean parse() {
        try {
            return program();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private boolean program() throws ParseException {
        if (match("robot_r")) {
            if (match("vars")) {
                if (varList()) {
                    if (match("procs")) {
                        if (procList()) {
                            return block();
                        }
                    }
                }
            }
        }
        throw new ParseException("Error en la sintaxis del programa", currentToken);
    }

    private boolean varList() throws ParseException {
        if (match("(")) {
            while (true) {
                if (match(IDENTIFIER)) {
                    if (match(";")) {
                        continue;
                    } else {
                        break; 
                    }
                } else {
                    break; 
                }
            }
            if (match(")")) {
                return true;
            }
        }
    
        throw new ParseException("Error en la sintaxis de la lista de variables", currentToken);
    }
    

    private boolean procList() throws ParseException {
        if (match("(")) {
            while (true) {
                if (match(IDENTIFIER)) {
                    if (match("(")) {
                        if (paramList()) {
                            if (match(")")) {
                                if (block()) {
                                    continue;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (match(")")) {
                return true;
            }
        }
        throw new ParseException("Error en la sintaxis de la lista de procedimientos", currentToken);
    }

    private boolean paramList() throws ParseException {
        if (match(IDENTIFIER)) {
            while (true) {
                if (match(",")) {
                    if (match(IDENTIFIER)) {
                        continue;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private boolean block() throws ParseException {
        if (match("[")) {
            while (true) {
                if (instruction()) {
                    continue;
                } else {
                    break;
                }
            }
            if (match("]")) {
                return true;
            }
        }
        throw new ParseException("Error en la sintaxis del bloque", currentToken);
    }

    private boolean instruction() throws ParseException {
        if (match(IDENTIFIER)) {
            if (match(":")) {
                if (exprList()) {
                    if (match(";")) {
                        return true;
                    }
                }
            }
        } else if (match("if")) {
            if (condition()) {
                if (block()) {
                    if (match("else")) {
                        if (block()) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        } else if (match("while")) {
            if (condition()) {
                if (block()) {
                    return true;
                }
            }
        } else if (match("repeat")) {
            if (expr()) {
                if (block()) {
                    return true;
                }
            }
        } else if (match(COMMANDS)) {
            if (argList()) {
                if (match(";")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean exprList() throws ParseException {
        if (expr()) {
            while (true) {
                if (match(",")) {
                    if (expr()) {
                        continue;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private boolean condition() throws ParseException {
        if (match(CONDITIONS)) {
            if (argList()) {
                return true;
            }
        } else if (match("not")) {
            if (condition()) {
                return true;
            }
        }
        return false;
    }

    private boolean argList() throws ParseException {
        if (expr()) {
            while (true) {
                if (match(",")) {
                    if (expr()) {
                        continue;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private boolean expr() throws ParseException {
        if (match(IDENTIFIER)) {
            return true;
        } else if (match(NUMBER)) {
            return true;
        } else if (match("(")) {
            if (expr()) {
                if (match(")")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean match(Map<String, Integer> identifier2) {
        if (currentToken < tokens.size() && tokens.get(currentToken).equals(identifier2)) {
            currentToken++;
            return true;
        }
        return false;
    }

    /**
     * @param string
     * @return
     */
    private boolean match(String string) {
        if (currentToken < tokens.size()) {
            boolean matcher = string.matches(tokens.get(currentToken)); 
        }
        return (Boolean) null;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("robot_program.txt");
        Parser parser = new Parser(file);
        if (parser.parse()) {
            System.out.println("El programa es sintácticamente correcto");
        } else {
            System.out.println("El programa tiene errores de sintaxis");
        }
    }
}




            


