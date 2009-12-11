package taocp.v4.f0.booleanevaluation;

public class TicTacToe {
	int count = 0;
	/**
	 * Count the valid legal inputs of 3*3 tic tac toe.
	 * just verify something in the book
	 * @return
	 */
	public int count() {
		
		for(int x1 = 0; x1 <=1; x1++)
			for(int x2 = 0; x2 <=1; x2++)
				for(int x3 = 0; x3 <=1; x3++)
					for(int x4 = 0; x4 <=1; x4++)
						for(int x5 = 0; x5 <=1; x5++)
							for(int x6 = 0; x6 <=1; x6++)
								for(int x7 = 0; x7 <=1; x7++)
									for(int x8 = 0; x8 <=1; x8++)
										for(int x9 = 0; x9 <=1; x9++)
											for(int o1 = 0; o1 <=1; o1++)
												for(int o2 = 0; o2 <=1; o2++)
													for(int o3 = 0; o3 <=1; o3++)
														for(int o4 = 0; o4 <=1; o4++)
															for(int o5 = 0; o5 <=1; o5++)
																for(int o6 = 0; o6 <=1; o6++)
																	for(int o7 = 0; o7 <=1; o7++)
																		for(int o8 = 0; o8 <=1; o8++)
																			for(int o9 = 0; o9 <=1; o9++){
																				_count(x1, x2, x3, x4, x5, x6, x7, x8, x9, o1, o2, o3, o4, o5, o6, o7, o8, o9);
																			}
		return count;
	}

	int count1 = 0;
	int count2 = 0;
	int count3 = 0;//both x and o
	int count4 = 0;
	int count5 = 0;
	private void _count(int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9, int o1, int o2, int o3, int o4, int o5, int o6, int o7, int o8, int o9) {
		if(x1+o1==2 || x2+o2 ==2 || x3+o3 == 2 || x4+o4 == 2 || x5+o5==2 || x6+o6==2 || x7+o7==2 || x8+o8==2 || x9+o9==2){
			count1++;
		}else{
			int xtotal = (x1 + x2 + x3 + x4 + x5 + x6 + x7 + x8 + x9);
			int ototal = (o1 + o2 + o3 + o4 + o5 + o6 + o7 + o8 + o9);
			int temp = xtotal - ototal;
			int total = xtotal + ototal;
			if(total == 9){
				count2++;
			}else if ((temp == 1 || temp == 0)  ) {
				if (x1 == 1 && x2 == 1 && x3 == 1||
						x4==1&&x5==1&&x6==1||
						x7==1&&x8==1&&x9==1||
						x1==1&&x4==1&&x7==1||
						x2==1&&x5==1&&x8==1||
						x3==1&&x6==1&&x9==1||
						x1==1&&x5==1&&x9==1||
						x1==3&&x5==1&&x7==1){
					count3++;
				}else if(o1==1&&o2==1&&o3==1||
						o4==1&&o5==1&&o6==1||
						o7==1&&o8==1&&o9==1||
						o1==1&&o4==1&&o7==1||
						o2==1&&o5==1&&o8==1||
						o3==1&&o6==1&&o9==1||
						o1==1&&o5==1&&o9==1||
						o1==3&&o5==1&&o7==1){
					count4++;
				}else{
					count ++;
				}
			}else{
				count5++;
			}
			
			
		}
		//return count;
	}
	
	public static void  main (String[] args){
		TicTacToe ttt = new TicTacToe();
		System.out.println(ttt.count());
		System.out.println(ttt.count1);
		System.out.println(ttt.count2);
		System.out.println(ttt.count3);
		System.out.println(ttt.count4);
		System.out.println(ttt.count5);
		System.out.println(ttt.count + ttt.count1 + ttt.count2 + ttt.count3 + ttt.count4 + ttt.count5);
		System.out.println(81*81*3);
		System.out.println(ttt.count + ttt.count2 + ttt.count3 + ttt.count4 + ttt.count5);
	}
}
