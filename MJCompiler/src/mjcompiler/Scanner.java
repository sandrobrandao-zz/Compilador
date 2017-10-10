package mjcompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.StringCharacterIterator;

/**
 *
 * @author Alvaro Bottura Santos
 */
public class Scanner {

    private static String input;
    private StringCharacterIterator inputIt;
    //private SymbolTable st;
    private int lineNumber;

    public Scanner(/*SymbolTable globalST, */String inputFileName) {
        File inputFile = new File(inputFileName);
        //st = globalST;

        try {
            FileReader fr = new FileReader(inputFile);

            int size = (int) inputFile.length();
            char[] buffer = new char[size];

            fr.read(buffer, 0, size);

            input = new String(buffer);

            inputIt = new StringCharacterIterator(input);

            lineNumber = 1;
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado");
        } catch (IOException e) {
            System.err.println("Erro na leitura do arquivo");
        }
    }

    public Token nextToken() {
        Token tok = new Token(EnumToken.UNDEF);

        int begin = 0, end = 0;
        String lexema = "";
        char ch = inputIt.current();

        while (true) {
            //Consome espaços em branco e volta para o estado inicial
            if(Character.getType(inputIt.current()) == '\0'){
                tok.name = EnumToken.EOF;
                tok.attribute = EnumToken.EOF;
                tok.lineNumber = lineNumber;
                return tok;
            
            }
            
            
            
            
            //conta ENTER's e avança espaços em branco
            
            if ( inputIt.current() == '\t' || inputIt.current() == '\r' || inputIt.current() == '\f' || inputIt.current() == ' ') {
                inputIt.next();
                if(inputIt.current() == '\n'){
                    inputIt.next();
                    lineNumber++;
                }
                while ( inputIt.current() == '\t' || inputIt.current() == '\r' || inputIt.current() == '\f' || inputIt.current() == ' ') {
                    inputIt.next();
                    if(inputIt.current() == '\n'){
                        inputIt.next();
                        lineNumber++;
                    }
                }
            }
            //Ignora comentários
            if (inputIt.current() == '/') {
                //COMENTARIO "/**/"
               switch(inputIt.next()){
                   case '*':
                       while(true)
                        {
                            if(inputIt.current() == '\n')
                            {
                                lineNumber++;
                                tok.lineNumber = lineNumber;
                                inputIt.next();
                            }
                            else if(inputIt.current() == '*')
                            {
                                inputIt.next();
                                if(inputIt.current() == '/'){
                                    
                                    inputIt.next();
                                    break;
                                }
                            }
                            else{
                                inputIt.next();
                            }
                        }
                        if(inputIt.current() == '\n'){
                            lineNumber++;
                        }
                        //inputIt.next();
                        tok.lineNumber = lineNumber;
                    break;
                   case '/':
                       while(true){
                           if(inputIt.current() == '\n'){
                               lineNumber++;
                               inputIt.next();
                               break;
                           }
                           inputIt.next();
                       }
                    break;
                   default:
                    tok.attribute = EnumToken.DIV;
                    tok.name = EnumToken.DIV;
                    return tok;
               }

            }

            //VERIFICANDO TOKEN digito inteiro
            if (Character.isDigit(inputIt.current())) {
                lexema += inputIt.current();
                inputIt.next();
                while (Character.isDigit(inputIt.current())) {
                    lexema += inputIt.current();
                    inputIt.next();
                }
                tok.name = EnumToken.INTEGER_LITERAL;
                tok.attribute = EnumToken.INTEGER_LITERAL;
                tok.value = lexema;
                tok.lineNumber = lineNumber;
                return tok;
            }
            //VERIFICANDO TOKEN ID e Palavras RESERVADAS
            if (Character.isLetter(inputIt.current())) {
                lexema += inputIt.current();
                inputIt.next();
                while (Character.isLetter(inputIt.current()) || Character.isDigit(inputIt.current()) || inputIt.current() == '_') {
                    lexema += inputIt.current();
                    inputIt.next();
                }
                if (lexema.matches("System")) {
                    if (inputIt.current() == '.') {
                        inputIt.next();
                        if (inputIt.current() == 'o') {
                            inputIt.next();
                            if (inputIt.current() == 'u') {
                                inputIt.next();
                                if (inputIt.current() == 't') {
                                    inputIt.next();
                                    if (inputIt.current() == '.') {
                                        inputIt.next();
                                        if (inputIt.current() == 'p') {
                                            inputIt.next();
                                            if (inputIt.current() == 'r') {
                                                inputIt.next();
                                                if (inputIt.current() == 'i') {
                                                    inputIt.next();
                                                    if (inputIt.current() == 'n') {
                                                        inputIt.next();
                                                        if (inputIt.current() == 't') {
                                                            inputIt.next();
                                                            if (inputIt.current() == 'l') {
                                                                inputIt.next();
                                                                if (inputIt.current() == 'n') {
                                                                    tok.name = EnumToken.SOPRINTLN;
                                                                    tok.attribute = EnumToken.SOPRINTLN;
                                                                    tok.value = lexema+".out.println";
                                                                    tok.lineNumber = lineNumber;
                                                                    inputIt.next();
                                                                    return tok;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (lexema.matches("boolean")) {
                    tok.name = EnumToken.BOOLEAN;
                    tok.attribute = EnumToken.BOOLEAN;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("class")) {
                    tok.name = EnumToken.CLASS;
                    tok.attribute = EnumToken.CLASS;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("else")) {
                    tok.name = EnumToken.ELSE;
                    tok.attribute = EnumToken.ELSE;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("extends")) {
                    tok.name = EnumToken.EXTENDS;
                    tok.attribute = EnumToken.EXTENDS;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("false")) {
                    tok.name = EnumToken.FALSE;
                    tok.attribute = EnumToken.FALSE;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("if")) {
                    tok.name = EnumToken.IF;
                    tok.attribute = EnumToken.IF;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("int")) {
                    tok.name = EnumToken.INT;
                    tok.attribute = EnumToken.INT;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("lenght")) {
                    tok.name = EnumToken.LENGTH;
                    tok.attribute = EnumToken.LENGTH;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("main")) {
                    tok.name = EnumToken.MAIN;
                    tok.attribute = EnumToken.MAIN;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("new")) {
                    tok.name = EnumToken.NEW;
                    tok.attribute = EnumToken.NEW;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("public")) {
                    tok.name = EnumToken.PUBLIC;
                    tok.attribute = EnumToken.PUBLIC;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("return")) {
                    tok.name = EnumToken.RETURN;
                    tok.attribute = EnumToken.RETURN;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("static")) {
                    tok.name = EnumToken.STATIC;
                    tok.attribute = EnumToken.STATIC;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("String")) {
                    tok.name = EnumToken.STRING;
                    tok.attribute = EnumToken.STRING;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("this")) {
                    tok.name = EnumToken.THIS;
                    tok.attribute = EnumToken.THIS;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("true")) {
                    tok.name = EnumToken.TRUE;
                    tok.attribute = EnumToken.TRUE;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("while")) {
                    tok.name = EnumToken.WHILE;
                    tok.attribute = EnumToken.WHILE;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                if (lexema.matches("void")) {
                    tok.name = EnumToken.VOID;
                    tok.attribute = EnumToken.VOID;
                    tok.value = lexema;
                    tok.lineNumber = lineNumber;
                    //inputIt.next();
                    return tok;
                }
                tok.name = EnumToken.ID;
                tok.attribute = EnumToken.ID;
                tok.value = lexema;
                tok.lineNumber = lineNumber;
                //inputIt.next();
                return tok;
            }
            

            //Operadores aritméticos
            if (inputIt.current() == '+' || inputIt.current() == '-'
                    || inputIt.current() == '*'
                    || inputIt.current() == '%' || inputIt.current() == '<' || inputIt.current() == '>') {
                //tok.name = EnumToken.ARITHOP;
                

                switch (inputIt.current()) {
                    case '+':
                        tok.name = EnumToken.PLUS;
                        tok.attribute = EnumToken.PLUS;
                        break;
                    case '-':
                        tok.name = EnumToken.MINUS;
                        tok.attribute = EnumToken.MINUS;
                        break;
                    case '*':
                        tok.name = EnumToken.MULT;
                        tok.attribute = EnumToken.MULT;
                        break;
                    case '<':
                        tok.name = EnumToken.LT;
                        tok.attribute = EnumToken.LT;
                        break;
                    case '>':
                        tok.name = EnumToken.GT;
                        tok.attribute = EnumToken.GT;
                        break;

                }

                inputIt.next();

                tok.lineNumber = lineNumber;

                return tok;
            }
            //verificando os Token OPERADORES
            //OPERADOR AND
            if (inputIt.current() == '&' && inputIt.next() == '&') {
                tok.name = EnumToken.AND;
                tok.attribute = EnumToken.AND;
                inputIt.next();
                tok.lineNumber = lineNumber;
                return tok;
            }
            //OPERADOR EQ e ATRIB
            if (inputIt.current() == '=') {
                inputIt.next();
                if (inputIt.current() == '=') {
                    tok.name = EnumToken.EQ;
                    tok.attribute = EnumToken.EQ;
                    inputIt.next();
                    tok.lineNumber = lineNumber;
                    return tok;
                } else {
                    tok.name = EnumToken.ATTRIB;
                    tok.attribute = EnumToken.ATTRIB;
                    tok.lineNumber = lineNumber;
                    return tok;
                }

            }
            //OPERADOR NE e NOT
            if (inputIt.current() == '!') {
                inputIt.next();
                if (inputIt.current() == '=') {
                    tok.name = EnumToken.NE;
                    tok.attribute = EnumToken.NE;
                    inputIt.next();
                    tok.lineNumber = lineNumber;
                    return tok;
                } else {
                    tok.name = EnumToken.NOT;
                    tok.attribute = EnumToken.NOT;
                    inputIt.next();
                    tok.lineNumber = lineNumber;
                    return tok;
                }

            }
            //VERIFICANDO CARACTERES DE SEPARAÇÂO DE BLOCOS E PONTUAÇÔES
            if (inputIt.current() == '(' || inputIt.current() == ')'
                    || inputIt.current() == '[' || inputIt.current() == ']'
                    || inputIt.current() == '{' || inputIt.current() == '}' || inputIt.current() == ';'
                    || inputIt.current() == '.' || inputIt.current() == ',') {
                //tok.name = EnumToken.UNDEF;

                switch (inputIt.current()) {
                    case '(':
                        tok.name = EnumToken.LPARENTHESE;
                        tok.attribute = EnumToken.LPARENTHESE;
                        break;
                    case ')':
                        tok.name = EnumToken.RPARENTHESE;
                        tok.attribute = EnumToken.RPARENTHESE;
                        break;
                    case '[':
                        tok.name = EnumToken.LBRACKET;
                        tok.attribute = EnumToken.LBRACKET;
                        break;
                    case ']':
                        tok.name = EnumToken.RBRACKET;
                        tok.attribute = EnumToken.RBRACKET;
                        break;
                    case '{':
                        tok.name = EnumToken.LBRACE;
                        tok.attribute = EnumToken.LBRACE;
                        break;
                    case '}':
                        tok.name = EnumToken.RBRACE;
                        tok.attribute = EnumToken.RBRACE;
                        break;
                    case ';':
                        tok.name = EnumToken.SEMICOLON;
                        tok.attribute = EnumToken.SEMICOLON;
                        break;
                    case '.':
                        tok.name = EnumToken.PERIOD;
                        tok.attribute = EnumToken.PERIOD;
                        break;
                    case ',':
                        tok.name = EnumToken.COMMA;
                        tok.attribute = EnumToken.COMMA;
                        break;
                }

                inputIt.next();

                tok.lineNumber = lineNumber;

                return tok;
            }

            //Continua....
        }//nextToken
    }
}
