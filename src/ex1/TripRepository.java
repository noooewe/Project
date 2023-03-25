package ex1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



public class TripRepository {
	private static TripRepository triprepository = new TripRepository();
	private TripRepository() {}
	public static TripRepository getInstance() {
		return triprepository;
	}
	
	public static List<TripDTO> tripDTOList = new ArrayList<>();
	public static List<UserDTO> userDTOList = new ArrayList<>();


	public boolean saveUser(UserDTO user) {
		System.out.println("아래 정보로 회원가입을 진행합니다.");
		System.out.println(user.toString());
		userDTOList.add(user);
		return true;
	}
	public boolean loginCheck(String email, String password) {

		for(UserDTO u : userDTOList){
			if(u.getEmail().equals(email) && u.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}


	public List<UserDTO> findAll() {
		return this.userDTOList;
	}


	public UserDTO findByEmail(String email, String password) {
		for(UserDTO u : userDTOList){
			if(u.getEmail().equals(email) && u.getPassword().equals(password)) {
				return u;
			}
		}
		return null;
	}



	public boolean ticketing(TripDTO tripDTO) {
		tripDTO.setPaymenttime(LocalDateTime.now());
		tripDTOList.add(tripDTO);
		System.out.println("예약번호는 " + tripDTO.getBno() + "입니다.");
		return true;
	}


	public TripDTO getReservation(String bno) {

		for (TripDTO tripDTO:tripDTOList){
			if (tripDTO.getBno() != null && tripDTO.getBno().equals(bno)) {
	            return tripDTO;
	        }
		}
	    return null;
	}


	public boolean updateUser(String email, String password, UserDTO userDto) {

		for(UserDTO u : userDTOList){
			if(u.getEmail().equals(email) && u.getPassword().equals(password)){

				if (!userDto.getPassword().isBlank()){ //isBlank(): 메서드는 String타입의 메서드이다.
					                                   //문자열이 비어있거나 모두 공백문자로 이루어져 있는지 확인하는 메서드이
					u.setPassword(userDto.getPassword());
				}

				if (!userDto.getName().isBlank()){
					u.setName(userDto.getName());
				}

				u.setAge(userDto.getAge());
				return true;

			}
		}
		return false;
	}


	public boolean deleteUser(String email, String password) {

		for(UserDTO u : userDTOList){
			if(u.getEmail().equals(email) && u.getPassword().equals(password)){
				userDTOList.remove(u);
				return true;
			}
		}

		return false;
	}
	

}
