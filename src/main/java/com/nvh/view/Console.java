package com.nvh.view;

import com.nvh.controller.Grille;
import com.nvh.controller.Scrabble;

public  class Console 
{
	public static void display(Grille g)
	{
		System.out.print("   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15"+"\n");
		System.out.print(" ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐"+"\n");  

		for (int i = 0; i < 29; i++) 
		{
			if (i%2==0)
			{  
				
				System.out.print(Scrabble.posLettres.charAt(i/2)+"│");
				for (int j = 0; j < 15; j++)
					if (g.getCoor()[j][i/2]!=' '
					//&& g.getCoor()[j][i/2]!='#'
					) {
						if (g.getBonus()[j][i/2]==0) 
							System.out.print(" "+g.getCoor()[j][i/2]+"*│");
						else System.out.print(" "+g.getCoor()[j][i/2]+" │");
					}
					
					else 		System.out.print("   "+"│");
			}
			else
				System.out.print("\n"+" ├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤"+"\n");
		}
		System.out.println("\n"+" └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘");
	}
}

