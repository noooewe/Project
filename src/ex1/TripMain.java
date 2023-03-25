package ex1;



public class TripMain {

	static boolean loginOn = false;
	public static void main(String[] args) {
		TripService tripservice = TripService.getInstance();
		Util util = new Util();


        while(true) {
        	System.out.println("===============행복투어===============");

//        	if(loginOn) {
//        		System.out.println("[ 1. 회원정보 : 2. 항공권구매 : 3. 예약내역 : 4. 정보수정 : 5. 로그아웃 : 6. 회원탈퇴 : 0. 종료 ]");
//        	} else if(!loginOn) {
//        		System.out.println("[ 1. 예약내역 : 2. 뒤로가기 : 0. 종료 ]");
//        	} else {
//        		System.out.println("[ 1. 회원가입 : 2. 로그인 : 3. 리스트 : 4. 항공권 구매(비회원 전용) : 0. 종료 ]");
//        	}

			if (loginOn){
				System.out.println("[ 1. 회원정보 : 2. 항공권 구매 : 3. 예약내역 : 4. 회원정보 수정 : 5. 로그아웃 : 6. 회원탈퇴 : 0. 종료 ]");
			}else {
        		System.out.println("[ 1. 회원가입 : 2. 로그인 : 3. 회원 리스트 : 4. 항공권 구매(비회원 전용) : 0. 종료 ]");
        	}
        	System.out.println("메뉴선택 >>");
        	int menu = util.numberCheck();

        	if(menu == 1) {
        		if(loginOn) {
					// 1. 회원정보
        			tripservice.findByEmail();
        		} else {
					// 1. 회원가입
        			tripservice.saveUser();
        		}
        	}else if (menu == 2) {
        		if(loginOn) {
        			tripservice.ticketing();
            	} else {
            		loginOn = tripservice.loginCheck();
            	}

        	}else if(menu == 3) {
        		if(loginOn) {
        			tripservice.getReservation();
        		} else {
        			tripservice.findAll();    			
        		}
        	}else if(menu == 4 && loginOn) {
        		tripservice.update();
        	}else if(menu == 5 && loginOn) {
        		tripservice.logout();
        		loginOn = false;
        	}else if(menu == 6 && loginOn) {
        		loginOn = tripservice.delete();
        	}else if(menu == 0) {
				System.out.println("프로그램 종료");
        		break;
        	}else {
        		System.out.println("다시 입력하십시오");
        	}
        	System.out.println();
        }


	}

}
