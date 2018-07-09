package com.freeme.freemelite.attendance.machinehelper;

import com.freeme.freemelite.attendance.machinehelper.model.EmployeeModel;

import java.util.Comparator;

public class EmployeeComparator implements Comparator<EmployeeModel> {

	public int compare(EmployeeModel o1, EmployeeModel o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return 1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return -1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
