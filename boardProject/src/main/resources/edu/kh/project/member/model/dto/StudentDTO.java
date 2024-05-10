package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class StudentDTO {
	
	private int studentNumber;
	private String studentName;
	private int koreanScore;
	private int englishScore;
	private int mathScore;

}
