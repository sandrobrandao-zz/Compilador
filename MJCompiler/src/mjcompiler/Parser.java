package mjcompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.StringCharacterIterator;

/**
 *
 * @author Alvaro Bottura Santos, Ricardo Yuuki Okishima
 */

public class Parser 
{
    private Scanner scan;
    //private SymbolTable globalST;
    //private SymbolTable currentST;
    private Token lToken;
    
    public Parser(String inputFile)    
    {
        //Instancia a tabela de sÃ­mbolos global e a inicializa
  //      globalST = new SymbolTable<STEntry>();
  //      initSymbolTable();
     
        //Faz o ponteiro para a tabela do escopo atual apontar para a tabela global
    //    currentST = globalST;
        
        //Instancia o analisador lÃ©xico
        scan = new Scanner(/*globalST, */inputFile);
        execute();
        
    }
    
    /*
     * MÃ©todo que inicia o processo de anÃ¡lise sintÃ¡tica do compilador
     */
    
    
    public void execute()
    {
        advance();
        
        try
        {
            program();
        }
        catch(CompilerException e)
        {
            System.err.println(e);
        }
    }
    
    private void advance()
    {
        lToken = scan.nextToken();
        
        System.out.println(lToken.name + "(" + lToken.lineNumber + ")" + " " );
    }
    
    private void match(EnumToken cTokenName) throws CompilerException
    {
        if (lToken.name == cTokenName)
            advance();
        else
        {            //Erro
            throw new CompilerException("Token inesperado: " + lToken.name);
        }
    }
    
    
    /*
     * MÃ©todo para o sÃ­mbolo inicial da gramÃ¡tica
     */    
    private void program() throws CompilerException
    {
        mainClass();
        listClassDeclaration();
        match(EnumToken.EOF);
        
        System.out.println("\nCompilaÃ§Ã£o encerrada com sucesso");
        
    }    
    
    private void mainClass() throws CompilerException
    {
        match(EnumToken.CLASS);
        match(EnumToken.ID);
        match(EnumToken.LBRACE);
        match(EnumToken.PUBLIC);
        match(EnumToken.STATIC);
        match(EnumToken.VOID);
        match(EnumToken.MAIN);
        match(EnumToken.LPARENTHESE);
        match(EnumToken.STRING);
        match(EnumToken.ID);
        match(EnumToken.RPARENTHESE);
        match(EnumToken.LBRACE);
        statement();
        match(EnumToken.RBRACE);
        match(EnumToken.RBRACE);
        
    }
    
    private void listClassDeclaration()
    {
        if(lToken.name == EnumToken.CLASS)
        {       
            classDeclaration();
            listClassDeclaration();
        }
        else;
    }
    
    private void classDeclaration() throws CompilerException
    {
        match(EnumToken.CLASS);
        match(EnumToken.ID);
        extendes();
        match(EnumToken.LBRACE);
        listVar();
        listMethod();
        match(EnumToken.RBRACE);
        
    }
        
    private void extendes()
    {
        if(lToken.name == EnumToken.EXTENDS)
        {
            advance();
            match(EnumToken.ID);
        }
        else;
    }
    
    private void listVar()
    {
        varDeclaration();
        listVar2();
    }
    
    private void listVar2()
    {
        if(lToken.name == EnumToken.INT || lToken.name == EnumToken.BOOLEAN || lToken.name == EnumToken.ID)
        {
            varDeclaration();
            listVar2();
        }
        
        else;
    }
    
    private void listMethod()
    {
        methodDeclaration();
        listMethod2();
    }
    
    private void listMethod2()
    {
        if(lToken.name == EnumToken.PUBLIC)
        {
            methodDeclaration();
            listMethod2();
        }
        
        else;
    }
    
    private void varDeclaration()
    {
        type();
        match(EnumToken.ID);
        match(EnumToken.SEMICOLON);
    }
    
    private void methodDeclaration()
    {
        match(EnumToken.PUBLIC);
        type();
        match(EnumToken.ID);
        match(EnumToken.LPARENTHESE);
        listType();
        match(EnumToken.RPARENTHESE);
        match(EnumToken.LBRACE);
        listVar();
        listStatement();
        match(EnumToken.RETURN);
        expression();
        match(EnumToken.SEMICOLON);
        match(EnumToken.RBRACE);
    }
    
    private void listType()
    {
        if(lToken.name == EnumToken.INT || lToken.name == EnumToken.BOOLEAN || lToken.name == EnumToken.ID)
        {
            l1();
        }
        else;
    }
    
    private void l1()
    {
        type();
        match(EnumToken.ID);
        l2();
    }
    
    private void l2()
    {
        if(lToken.name == EnumToken.INT || lToken.name == EnumToken.BOOLEAN || lToken.name == EnumToken.ID)
        {
            type();
            match(EnumToken.ID);
            l2();
        }
        else;
    }
    
    private void listStatement()
    {
        if(lToken.name == EnumToken.LBRACE || lToken.name == EnumToken.IF || lToken.name == EnumToken.WHILE || lToken.name == EnumToken.SOPRINTLN || lToken.name == EnumToken.ID)
        {
            statement();
            listStatement();
        }
        else;
    }
    
    private void type()
    {
        if(lToken.name == EnumToken.INT)
        {
            advance();
            type2();
        }
        else if(lToken.name == EnumToken.BOOLEAN)
        {
            advance();
        }
        else if(lToken.name == EnumToken.ID)
        {
            advance();
        }
        else
        {
            System.err.println("Nao existe esta produÃ§Ã£o em type");
        }
            
    }
    
    private void type2()
    {
        if(lToken.name == EnumToken.LBRACKET)
        {
            advance();
            match(EnumToken.RBRACKET);
        }
    }
    
    private void statement()
    {
        if(lToken.name == EnumToken.LBRACE)
        {
            advance();
            listStatement();
            match(EnumToken.RBRACE);
        }
        else if(lToken.name == EnumToken.IF)
        {
            advance();
            match(EnumToken.LPARENTHESE);
            expression();
            match(EnumToken.RPARENTHESE);
            statement();
            match(EnumToken.ELSE);
            statement();
        }
        else if(lToken.name == EnumToken.WHILE)
        {
            advance();
            match(EnumToken.LPARENTHESE);
            expression();
            match(EnumToken.RPARENTHESE);
            statement();
        }
        else if(lToken.name == EnumToken.SOPRINTLN)
        {
            advance();
            match(EnumToken.LPARENTHESE);
            expression();
            match(EnumToken.RPARENTHESE);
            match(EnumToken.SEMICOLON);
        }
        else if(lToken.name == EnumToken.ID)
        {
            advance();
            idOptions();
        }
        else
        {
            System.err.println("Nao existe esta produÃ§Ã£o em statement");
        }
    }
    
    private void expression()
    {
        if(lToken.name == EnumToken.INTEGER_LITERAL)
        {
            advance();
            expression2();
        }
        else if(lToken.name == EnumToken.TRUE)
        {
            advance();
            expression2();
        }
        else if(lToken.name == EnumToken.FALSE)
        {
            advance();
            expression2();
        }
        else if(lToken.name == EnumToken.ID)
        {
            advance();
            expression2();
        }
        else if(lToken.name == EnumToken.THIS)
        {
            advance();
            expression2();
        }
        else if(lToken.name == EnumToken.NEW)
        {
            advance();
            neW();
        }
        else if(lToken.name == EnumToken.NOT)
        {
            advance();
            expression();
            expression2();
        }
        else if(lToken.name == EnumToken.LPARENTHESE)
        {
            advance();
            expression();
            match(EnumToken.RPARENTHESE);
            expression2();
        }
    }
    
    private void neW()
    {
        if(lToken.name == EnumToken.INT)
        {
            advance();
            match(EnumToken.LBRACKET);
            expression();
            match(EnumToken.RBRACKET);
            expression2();
        }
        else if(lToken.name == EnumToken.ID)
        {
            match(EnumToken.LPARENTHESE);
            match(EnumToken.RPARENTHESE);
            expression2();
        }
    }
    
    private void idOptions()
    {
        if(lToken.name == EnumToken.ATTRIB)
        {
            advance();
            expression();
            match(EnumToken.SEMICOLON);
        }
        else if(lToken.name == EnumToken.LBRACKET)
        {
            advance();
            expression();
            match(EnumToken.RBRACKET);
            match(EnumToken.ATTRIB);
            expression();
            expression2();
            match(EnumToken.SEMICOLON);
        }
        else
        {
            System.err.println("Nao existe esta produÃ§Ã£o em idOptions");
        }
    }
    
    private void expression2()
    {
        if(lToken.name == EnumToken.AND || lToken.name == EnumToken.LT || lToken.name == EnumToken.GT || lToken.name == EnumToken.EQ || lToken.name == EnumToken.NOT || lToken.name == EnumToken.NE || lToken.name == EnumToken.PLUS || lToken.name == EnumToken.MINUS || lToken.name == EnumToken.MULT || lToken.name == EnumToken.DIV)
        {
            op();
            expression();
            expression2();
        }
        else if(lToken.name == EnumToken.LBRACKET)
        {
            advance();
            expression();
            match(EnumToken.RBRACKET);
            expression2();
        }
        else if(lToken.name == EnumToken.PERIOD)
        {
            advance();
            pointer();
            expression2();
        }
        else;
    }    
    
    private void pointer()
    {
        if(lToken.name == EnumToken.LENGTH)
        {
            advance();
        }
        else if(lToken.name == EnumToken.ID)
        {
            advance();
            match(EnumToken.LPARENTHESE);
            le();
            match(EnumToken.RPARENTHESE);
        }
        else
        {
            System.err.println("Nao existe essa produÃ§Ã£o em pointer");
        }
    }
    
    private void le()
    {
        if(lToken.name == EnumToken.INTEGER_LITERAL || lToken.name == EnumToken.TRUE || lToken.name == EnumToken.FALSE || lToken.name == EnumToken.ID || lToken.name == EnumToken.THIS || lToken.name == EnumToken.NEW || lToken.name == EnumToken.NOT || lToken.name == EnumToken.LPARENTHESE)
        {
            le1();
        }    
        else;
    }
    
    private void le1()
    {
        expression();
        le2();
    }
    
    private void le2()
    {
        if(lToken.name == EnumToken.COMMA)
        {
            advance();
            expression();
            le2();
        }
        else;     
    }
    
    private void op()
    {
        if(lToken.name == EnumToken.AND || lToken.name == EnumToken.LT || lToken.name == EnumToken.GT || lToken.name == EnumToken.EQ || lToken.name == EnumToken.NE || lToken.name == EnumToken.PLUS || lToken.name == EnumToken.MINUS || lToken.name == EnumToken.MULT || lToken.name == EnumToken.DIV)
        {
            advance();
        }
    }
              
}