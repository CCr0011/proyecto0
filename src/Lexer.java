import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (c) {
                case '(':
                    tokens.add(new Token(TokenType.LPAREN, "("));
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RPAREN, ")"));
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";"));
                    break;
                default:
                    if (Character.isWhitespace(c)) {
              
                    } else if (Character.isDigit(c)) {

                        token.append(c);
                        while (i + 1 < input.length() && Character.isDigit(input.charAt(i + 1))) {
                            i++;
                            token.append(input.charAt(i));
                        }
                        tokens.add(new Token(TokenType.NUMBER, token.toString()));
                        token.setLength(0); 
                    } else if (Character.isLetter(c)) {
                        
                        token.append(c);
                        while (i + 1 < input.length() && (Character.isLetter(input.charAt(i + 1)) || input.charAt(i + 1) == '_')) {
                            i++;
                            token.append(input.charAt(i));
                        }
                        String word = token.toString();
                        switch (word.toLowerCase()) {
                            case "move":
                                tokens.add(new Token(TokenType.MOVE, word));
                                break;
                            case "turn":
                                tokens.add(new Token(TokenType.TURN, word));
                                break;
                           
                            default:
                                tokens.add(new Token(TokenType.IDENTIFIER, word));
                                break;
                        }
                        token.setLength(0); 
                    } else {
                        
                        throw new IllegalArgumentException("Carácter no reconocido: " + c + " en la posición: " + i);
                    }
                    break;
            }
        }

        return tokens;
    }
}
