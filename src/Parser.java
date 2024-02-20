import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int currentTokenIndex = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            parseInstruction();
        }
    }

    private void parseInstruction() {
        Token token = advance();

        switch (token.getType()) {
            case MOVE:
                parseMove();
                break;
            case TURN:
                parseTurn();
                break;
            // ... y así para cada tipo de instrucción
            default:
                // Manejo de error o caso no reconocido
        }
    }

    private void parseMove() {
        // Lógica para parsear un movimiento
    }

    private void parseTurn() {
        // Lógica para parsear un giro
    }

    // Métodos auxiliares como 'advance()' y 'isAtEnd()' aquí...

    private boolean isAtEnd() {
        return currentTokenIndex >= tokens.size();
    }

    private Token advance() {
        return tokens.get(currentTokenIndex++);
    }
}
