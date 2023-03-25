package ex1;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;


public class Util {
	Scanner sc = new Scanner(System.in);
	
	public int numberCheck() {
		if(sc.hasNextInt()) {
			return sc.nextInt();
		}else {
			sc.nextLine();
			return -1;
		}
	}


	
	public boolean isDuplicateEmail(List<UserDTO> userList , String inputEmail) {
		for(UserDTO u : userList) {
			if(u.getEmail().equals(inputEmail)) {
				return false;
			}
		}
		return true;
	}

	public String generateBno(){
		//String bno = "S" + (1000 + map.size()) + "-" + (2000 + map.size());
		Random r = new Random();
		//랜덤의 정수를 생성, 인스턴스(r) 객체 선언
		int aLow = 1000, aHigh = 9000;
		int bLow = 2000, bHigh = 10000;
		String bno = "S" + (r.nextInt(aHigh-aLow) + aLow) + "-" + (r.nextInt(bHigh-bLow) + bLow);
		//r.nextInt(aHigh-aLow) + aLow)을 입력하는 이유: aHigh-aLow 은 9000-1000이기에 8000의 값이 나온다. 하지만 랜덤으로 0~8000의 숫자가 출력된다
		//우린 1000~9000사이의 값을 원하기에 최소값 aLow를 더해줘 랜던으로 나온 정수가 무조건 최소값이상으로 나오게 한다. 
		return bno;
	}

}