import java.io.InputStreamReader;

/**
 * Parsimonious - a mathematical parser.
 * Known bugs: Will only process one line with each run. Does not do any maths. Does not validate grammar. No tree traversal.
 * Should use Exceptions rather than exiting. Does not yet accept ~ for negative numbers.
 * @author Wilfred Hughes
 */

// invalid syntax suggestions: "1.0.1" "sin" "css" "coos" "3**3" "2 co"

public class Parsimonious
{	public static void main(String[] args)
	{	//intro
		System.out.printf("The following operators are accepted in descending order of priority:%n");
		System.out.printf("cos ! * + - (cos in radians, ! only on integers)%n");
		System.out.printf("Signed floating point numbers are accepted in the forms 0, 0.0 or .0 (negative numbers must use ~) %n");
		System.out.printf("********************************************%n");
		System.out.printf("Type a mathematical expression and hit enter. All whitespace will be ignored.%n");

		InputStreamReader input = new InputStreamReader(System.in);

		String inputString = ""; //need to instantiate this outside the try block to keep java happy

		try
		{	int a = input.read();
			//put input in string
			while (a != -1 && a != 10) //-1 is end of stream, 10 is character return
			{	inputString = inputString + (char)a;
				a = input.read();
			}
		}
		catch (java.io.IOException e) 
		{	System.out.println("IOException! Exiting.");
			System.exit(1);
		}

		String strippedInput = Lexer.removeWhitespace(inputString);
		System.out.printf("Stripping whitespace: %s%n",strippedInput);

		System.out.printf("Separating String into tokens...");
		String[] tokenArray = Lexer.separateTokens(strippedInput);
		System.out.printf("OK%n");
		System.out.printf("Current String array: "); printArray(tokenArray);

		System.out.printf("Tokenising...");
		Token[] mathsArray = Lexer.tokenise(tokenArray);
		System.out.printf("OK%n");
		System.out.printf("Current Token array: "); printArray(mathsArray);
		
		//validate grammar
		//parse
		//Parser.parse(mathsArray);
		//System.out.printf("Parsed result: "); printArray(mathsArray);
	}

	private static void printArray(Object[] input) //accepts strings or tokens
	{	for (int i=0; i<input.length; i++)
		{	System.out.printf("%s ",input[i]);
		}
		System.out.printf("%n");
	}
}

class Token
{	private String operatorName;
	private boolean isOperator;
	private float number;

	public boolean isOperator()
	{	return isOperator;
	}

	public Token(String value)
	{	operatorName = value;
		isOperator = true;
	}

	public Token(float value)
	{	number = value;
		isOperator = false;
	}

	public String toString()
	{	if (isOperator)
		{	return operatorName;
		}
		else
		{	return "" + number;
		}
	}
}

class Lexer
{	public static String removeWhitespace(String input)
	{	String returnme = "";
		for (int i=0; i<input.length(); i++)
		{	if((int)input.charAt(i) != 9 && (int)input.charAt(i) != 32) //not tab or space
			{	returnme = returnme + input.charAt(i);
			}
		}
		return returnme;
	}

	public static String[] separateTokens(String input)
	{	String[] returnme = new String[0];
		for (int i=0; i<input.length(); i++)
		{	if (isNumeric(input.charAt(i)))
			{	if (i == 0) //we are at the start so don't look backwards
				{	returnme = extendArray(returnme,input.charAt(i)+"");
				}
				else
				{	if (isNumeric(returnme[returnme.length-1].charAt(0))) //last token is numeric, extend with this character
					{	returnme[returnme.length-1] = returnme[returnme.length-1] + input.charAt(i);
					}
					else //last token was operator, start new token
					{	returnme = extendArray(returnme,input.charAt(i)+"");
					}
				}
			}
			else
			{	try
				{	if (Lexer.isValidOperator(input.charAt(i)+""))
					{	returnme = extendArray(returnme,input.charAt(i)+""); //single character operator
					}
					else if (Lexer.isValidOperator("" + input.charAt(i) + input.charAt(i+1) + input.charAt(i+2)))
					{	returnme = extendArray(returnme,"" + input.charAt(i) + input.charAt(i+1) + input.charAt(i+2)); //3 character operator
						i += 2;
					}
					else
					{	System.out.printf("Neither '%s' nor '%s' are valid operators.%n",input.charAt(i),"" + input.charAt(i) + input.charAt(i+1) + input.charAt(i+2));
						System.exit(1);
					}
				}
				catch (StringIndexOutOfBoundsException e)
				{	System.out.printf("Invalid operator length.%n");
					System.exit(1);
				}
			}
		}
		return returnme;
	}

	public static Token[] tokenise(String[] tokenStrings)
	{	Token[] returnme = new Token[tokenStrings.length];
		for (int i=0; i<tokenStrings.length; i++)
		{	if (isNumeric(tokenStrings[i].charAt(0)))
			{	try
				{	returnme[i] = new Token(Float.parseFloat(tokenStrings[i]));
				}
				catch (NumberFormatException e)
				{	System.out.printf("Not a recognised number: %s%n",e.getMessage());
					System.exit(1);
				}
			}
			else
			{	returnme[i] = new Token(tokenStrings[i]);	
			}
		}
		return returnme;
	}

	//inefficient but quick and dirty
	private static String[] extendArray(String[] input, String element)
	{	//if statement due to nasty empty array corner case
		String[] returnme = new String[input.length+1];
		int i;
		for (i=0; i<returnme.length; i++)
		{	if (i == input.length)
			{	returnme[i] = element;
			}
			else
			{	returnme[i] = input[i];
			}
		}		
		return returnme;
	}

	private static boolean isNumeric(char input)
	{	if (input == '0' || input == '1' || input == '2' || input == '3' || 
		    input == '4' || input == '5' || input == '6' || input == '7' || 
		    input == '8' || input == '9' || input == '.' || input == '~')
		{	return true;
		}
		else
		{	return false;
		}
	}

	private static boolean isValidOperator(String input)
	{	if (input.equals("+") || input.equals("-") || input.equals("*") || input.equals("!") || input.equals("cos"))
		{	return true;
		}
		else
		{	return false;
		}
	}
}

class Parser
{	//coming soon
}

/* 
simplified grammar, showing precedence:
expr -> cos expr
expr -> expr !
expr -> expr * expr
expr -> expr + expr
expr -> expr - expr
expr -> real
real -> the set of real numbers

full grammar:
expr -> cos expr
expr -> expr !
expr -> expr * expr
expr -> expr + expr
expr -> expr - expr
expr -> digits.digits
digits -> digits digit | digit
digit -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
*/