package ex1;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TripService {
	private static TripService tripservice = new TripService();
	
	TripService() {}
	
	public static TripService getInstance() {
		return tripservice;
	}
	
	Scanner sc = new Scanner(System.in);
	TripRepository triprepository = TripRepository.getInstance();
	Util util = new Util();
	public static String loginEmail = null;
	public static String loginPassword = null;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH");


	public void saveUser() {
		while(true){
			UserDTO userDTO = new UserDTO();
			System.out.print("이메일 >> ");
			String email = sc.next();
			if (email.isBlank()){
				System.out.print("이메일을 다시 입력해주세요.");
				continue;
			}
			
			// 이메일 중복검사
			if (!util.isDuplicateEmail(triprepository.findAll(), email)){
				System.out.println("중복된 이메일 입니다. " + email);
				return;
			}

			userDTO.setEmail(email);
			System.out.print("비밀번호>> ");
			userDTO.setPassword(sc.next());

			System.out.print("이름>> ");
			userDTO.setName(sc.next());

			System.out.print("나이>> ");
			userDTO.setAge(sc.nextInt());
			userDTO.setJoinDate(LocalDateTime.now());
			if(triprepository.saveUser(userDTO)) {
				System.out.println("회원가입 성공!!");
				break;
			}else {
				System.out.println("회원가입 실패ㅠㅠ");
				break;
			}
		}

		
	}


	public void findAll() {
		System.out.println("이메일\t\t비밀번호\t이름\t나이\t가입일");
		System.out.println("--------------------------------------------------------");
		for(UserDTO dto: triprepository.findAll()){
			System.out.println(dto.toString());
		}
	}


	public boolean loginCheck() {
		System.out.print("이메일>> ");
		String email = sc.next();
		System.out.print("비밀번호>> ");
		String password = sc.next();
		System.out.println("입력하신 정보 : [이메일] " + email + " [비밀번호] " + password);

		if(triprepository.loginCheck(email, password)) {
			System.out.println("로그인 성공!!");
			loginEmail = email;
			loginPassword = password;
			return true;
		}else {
			System.out.println("로그인 실패ㅠㅠ");
			return false;
		}
	}

	public void findByEmail() {

	    if (loginEmail == null || loginPassword == null) {
	        System.out.println("로그인 오류!!");
	        return;
	    }

		UserDTO userDTO = triprepository.findByEmail(loginEmail, loginPassword);
	  
	    if (userDTO == null) {
	        System.out.println("세션오류");
	    } else {
	        System.out.println("이메일\t\t비밀번호\t이름\t나이\t가입일");
	        System.out.println("--------------------------------------------------------");
	        System.out.println(userDTO.toString());
	    }
	}


	public void ticketing() {
		
		while(true) {
			TripDTO tripDTO = new TripDTO();


			System.out.print("출발일을 입력하세요 (예: 2023/03/22/15)>> ");

			// 출발 시간 입력 받기
			String startTimeStr = sc.next();
			if (startTimeStr.isBlank()){
				System.out.println("출발일을 다시 입력해주세요.");
				System.out.println();
				continue;
			}

			try {
			    LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter); 
			    // 문자열을 LocalDateTime으로 변환
			    tripDTO.setStartTime(startTime);
			    // 출발일 설정
			} catch (DateTimeParseException e) {
			    System.out.println("유효하지 않은 날짜/시간 형식입니다. 다시 입력해주세요.");
			    continue;
			}

			LocalDateTime endTime = null;
			while (true) {
			    System.out.print("도착일을 입력하세요 (예: 2023/03/25/20)>> ");
				// 도착일 입력 받기
				String endTimeStr = sc.next();

			    if (endTimeStr.isBlank()) { // 입력 값이 빈 문자열인 경우
					System.out.println("도착일을 다시 입력하십시오.");
					System.out.println();
			        continue;
			    }

				//try,catch를 사용해 예외 발생이 있는 부분을 try안에 입력하고, 만약 예외발생이 할시 catch에서 처리되어 출력하고, continue를 이용해 다시 돌아가 입력을 받을수 있게 하였다.
			    try {
					// 출발일을 입력할때 처럼, 문자열을 LocalDateTime으로 변환해야 한다.
			        endTime = LocalDateTime.parse(endTimeStr, formatter);
			    } catch (DateTimeParseException e) {
			        System.out.println("유효하지 않은 날짜/시간 형식입니다. 다시 입력하십시오.");
					continue;
			    }

				if (endTime.isEqual(tripDTO.getStartTime())) { // 도착일자가 출발일자와 같을 경우
					System.out.println("도착일이 출발일과 같습니다. 다시 입력하십시오.");
					continue;
				} else if (endTime.isBefore(tripDTO.getStartTime())) { // 출발일보다 도착일자가 빠른 경우
					System.out.println("도착일이 출발일보다 빠릅니다. 다시 입력하십시오.");
					continue;
				} else if (endTime.toLocalDate().isEqual(tripDTO.getStartTime().toLocalDate()) &&
						endTime.isBefore(tripDTO.getStartTime().plusDays(1))) { // 출발일과 도착일이 1일 이상 차이나야 예약이 가능한 경우
					System.out.println("출발일과 도착일이 1일이상 차이나야 예약이 가능합니다. 다시 입력하십시오.");
					continue;
				}

				// 도착 시간 설정
				tripDTO.setEndTime(endTime);
				break;
			}

		   System.out.println("<<1. 아시아 || 2. 유럽 || 3. 아메리카 >>");
		   System.out.println("여행하고 싶은 대륙을 선택하세요>> ");

		   // 예약번호
			String bno = util.generateBno();
			tripDTO.setBno(bno);

		   int menu = sc.nextInt();

		   if(menu == 1) {
			 System.out.println("<<1. 일본 || 2. 중국>>");
			 System.out.println("국가를 선택하세요.>>");
			 int menu1 = sc.nextInt();
			 if(menu1 == 1) {
				 tripDTO.setCountry("일본");
				 System.out.println("<<1. 도쿄 || 2. 오사카 || 3. 후쿠오카 || 4. 홋카이도 || 5. 오키나와 >>");
				 System.out.println("도시를 선택하세요.>>");
				 int menu12 = sc.nextInt();
				 
				 if(menu12 == 1) {					 
					 tripDTO.setCtiy("도쿄");
					 System.out.println("<<1. 나리타 공항 || 2. 하네다 공항>>");
					 System.out.println("공항을 선택하세요>>");
					 int menu13 = sc.nextInt();
					 
					 if(menu13 == 1) {
						 tripDTO.setAirport("나리타 공항");
						 System.out.println("<<1. 퍼스트 || 2. 비지니스 || 3. 이코노미 >> ");
						 System.out.println("좌석등급을 선택하세요>> ");
						 int menu14 = sc.nextInt();
						 
						 if(menu14 == 1) {
							 tripDTO.setPlaneclass("퍼스트");
							 System.out.println("<<1. 창가 || 2. 통로 || 3. 가운데 >> ");
							 System.out.println("좌석위치를 선택하세요>> ");
							 int menu15 = sc.nextInt();
							 
							 if(menu15 == 1) {
								 tripDTO.setSeatposition("창가");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }
							 }

							 if(menu15 == 2) {
								 tripDTO.setSeatposition("통로");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();
								 
								 if(menu16 == 1) {
									 System.out.println("결제완료!");
		                             triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }
								 
							 }
							 if(menu15 == 3) {
								 tripDTO.setSeatposition("가운데");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
						 }else if(menu14 == 2) {
							 tripDTO.setPlaneclass("비지니스");
							 System.out.println("<<1. 창가 || 2. 통로 || 3. 가운데 >> ");
							 System.out.println("좌석위치를 선택하세요>> ");
							 int menu15 = sc.nextInt();
							 
							 if(menu15 == 1) {
								 tripDTO.setSeatposition("창가");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 2) {
								 tripDTO.setSeatposition("통로");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 3) {
								 tripDTO.setSeatposition("가운데");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
						 } if(menu14 == 3) {
							 System.out.println("<<1. 창가 || 2. 통로 || 3. 가운데 >> ");
							 System.out.println("좌석위치를 선택하세요>> ");
							 int menu15 = sc.nextInt();
							 
							 if(menu15 == 1) {
								 tripDTO.setSeatposition("창가");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 2) {
								 tripDTO.setSeatposition("통로");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 3) {
								 tripDTO.setSeatposition("가운데");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
						 }
					 } if(menu13 == 2) {
						 tripDTO.setAirport("하네다 공항");
						 System.out.println("<<1. 퍼스트 || 2. 비지니스 || 3. 이코노미 >> ");
						 System.out.println("좌석등급을 선택하세요>> ");
						 int menu14 = sc.nextInt();
						 
						 if(menu14 == 1) {
							 tripDTO.setPlaneclass("퍼스트");
							 System.out.println("<<1. 창가 || 2. 통로 || 3. 가운데 >> ");
							 System.out.println("좌석위치를 선택하세요>> ");
							 int menu15 = sc.nextInt();
							 
							 if(menu15 == 1) {
								 tripDTO.setSeatposition("창가");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 2) {
								 tripDTO.setSeatposition("통로");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 3) {
								 tripDTO.setSeatposition("가운데");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
						 } if(menu14 == 2) {
							 tripDTO.setPlaneclass("비지니스");
							 System.out.println("<<1. 창가 || 2. 통로 || 3. 가운데 >> ");
							 System.out.println("좌석위치를 선택하세요>> ");
							 int menu15 = sc.nextInt();
							 
							 if(menu15 == 1) {
								 tripDTO.setSeatposition("창가");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 2) {
								 tripDTO.setSeatposition("통로");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 3) {
								 tripDTO.setSeatposition("가운데");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
						 } if(menu14 == 3) {
							 tripDTO.setPlaneclass("이코노미");
							 System.out.println("<<1. 창가 || 2. 통로 || 3. 가운데 >> ");
							 System.out.println("좌석위치를 선택하세요>> ");
							 int menu15 = sc.nextInt();
							 
							 if(menu15 == 1) {
								 tripDTO.setSeatposition("창가");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 2) {
								 tripDTO.setSeatposition("통로");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }

							 }
							 if(menu15 == 3) {
								 tripDTO.setSeatposition("가운데");
								 System.out.println("<<1. 신용카드 || 2. 계좌이체>> ");
								 System.out.println("결제 수단을 선택하세요>> ");
	                             int menu16 = sc.nextInt();

								 if(menu16 == 1) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 } else if(menu16 == 2) {
									 System.out.println("결제완료!");
									 triprepository.ticketing(tripDTO);
								 }
							 }
						 }
					 }
				 	 				 
				 }else if(menu12 == 2) {
					 tripDTO.setCtiy("오사카");
				 }else if(menu12 == 3) {
					 tripDTO.setCtiy("후쿠오카");
				 }else if(menu12 == 4) {
					 tripDTO.setCtiy("훗카이도");
				 }else if(menu12 == 5) {
					 tripDTO.setCtiy("오키나와");
				 }else if(menu12 == 0) {
					 continue;
				 }else {
					 System.out.println("다시 입력하세요!!>> ");
				 }
			 }else if(menu1 == 2) {
				 tripDTO.setCountry("중국");
			 }
		 }else if(menu == 2) {
			 
		 }else if(menu == 3) {
			 
		 }else {
				System.out.println("잘못된 결제 수단입니다.");
			}		   
			System.out.println("계속 예약하시겠습니까? (1. 예, 0. 아니오)>> ");
			int continueBooking = sc.nextInt();
			if (continueBooking == 0) {
				break;
			}
		 }
		
	}
        
	   
	public void getReservation() {
	    while (true) {
	        System.out.print("예약번호를 입력하세요.>> ");
	        String bno = sc.next();
	        TripDTO tripDTO = triprepository.getReservation(bno);
	        if (tripDTO == null) {
	            System.out.println("예약내역이 없습니다. 예약번호를 다시 확인하세요.");
	        } else {
	            System.out.println("예약번호\t\t출발일\t\t\t도착일\t\t\t국가\t도시\t공항\t\t비행기클래스\t좌석위치\t결제일시");
	            System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
	            System.out.println(tripDTO.toPurchaseString());
	            break;
	        }
	        sc.nextLine(); // 입력 버퍼를 비워줌
	    }
	}
	
	public void update() {
		while(true) {
			UserDTO user = new UserDTO();
			System.out.print("수정할 비밀번호>> ");
			String password = sc.next();

			if (password.isBlank()){
				System.out.print("수정할 비밀번호를 다시 입력해주세요");
				continue;
			}
			user.setPassword(password);
			System.out.print("수정할 이름>> ");
			String name = sc.next();
			if (name.isBlank()){
				System.out.print("수정할 이름을 다시 입력해주세요");
				continue;
			}
			user.setName(name);
			System.out.print("수정할 나이>> ");
			user.setAge(sc.nextInt());
			if(triprepository.updateUser(loginEmail, loginPassword, user)){
				System.out.println("회원정보 업데이트 성공!!");
				loginPassword = user.getPassword();
				break;
			}else {
				System.out.println("회원정보 업데이트 실패ㅠㅠ");
				break;
			}
		}

	}
	public void logout() {
		loginEmail = null;
		loginPassword = null;
		System.out.println("로그아웃");
	}

	
	public boolean delete() {
	    while(true) {
	        System.out.print("계정을 탈퇴하시면 회원님의 모든 혜택이 소멸됩니다.\n정말로 삭제 하시겠습니까??(Y/N)>> ");
	        String d = sc.next();

	        if(d.equalsIgnoreCase("Y")) {
	        	//equalsIgnoreCase는 String 타입의 메서드로, 대소문자를 뭇시하고 두 문자열의 내용이 같은지 확인할 때 사용하는 메서드 입니다.
	            if(triprepository.deleteUser(loginEmail, loginPassword)){
	                System.out.println("그동안 저희 상품을 이용해주셔서 감사합니다. \n인연이 되면 다시 만나길 바라겠습니다.");
	                loginEmail = null;
	                loginPassword = null;
	                return false;
	            } else {
	                System.out.println("회원 탈퇴 실패했습니다.");
	                return true;
	            }
	        } else if(d.equalsIgnoreCase("N")) {
	            System.out.println("삭제를 취소합니다.");
	            return true;
	        } else {
	            System.out.println("잘못된 입력입니다. Y 또는 N을 입력해주세요.");
	        }
	    }
	}



}
