/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mjcompiler;
/**
 *
 * @author Alvaro Bottura Santos
 */
public class MJCompiler 
{
    public static void main(String[] args) 
    {       
        /*globalST = new SymbolTable<STEntry>();
        initSymbolTable();
        */
       // Scanner scanner = new Scanner(/*globalST, */"teste1.mj");
        
      //  Token tok;
        Parser parser = new Parser("teste1.mj");
        //double var = 2.e+10;
        
        /*do
        {
            tok = scanner.nextToken();
            System.out.print(tok.attribute + " ");
            System.out.print("(" + tok.value + ")");
            System.out.println("-----------" + "[" + tok.lineNumber + "]");
        } while (tok.name != EnumToken.EOF);
     */
    }

}
