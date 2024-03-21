package edu.kh.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/* Spring EL 같은 경우 getter가 필수로 작성되어야함!!!
-> ${Student.getName()}   ->    ${Student.name} 
getter 대신 필드명 호출하는 형식으로 해아 형식으로 작성하는데 자동으로 getter를 호출하기 때문*/

@Getter // 컴파일시 getter코드 자동 추가
@Setter // 컴파일시 Setter코드 자동 추가
@ToString // 컴파일시 toString() 메서드가 자도 ㅇ오버라이딩되서 추가
@NoArgsConstructor // 매개변수 없는 생성자(== 기본생성자)
@AllArgsConstructor // 모든 필드를 초기화하는 매개변수 생성자
public class Student {
	private String studentNo;
	private String name;
	private int age;


}
