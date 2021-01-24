import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class trial
{
  public static void main(String[] args)
                        throws FileNotFoundException
  {
    Scanner sc = new Scanner("Common.cfg");

    while (sc.hasNextLine())
        System.out.println(sc.nextLine());
  }
}
