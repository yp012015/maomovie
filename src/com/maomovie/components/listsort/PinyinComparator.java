package com.maomovie.components.listsort;

import com.maomovie.entity.SupportCityEntity;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<SupportCityEntity> {

	public int compare(SupportCityEntity o1, SupportCityEntity o2) {
		if (o1.getPinyin().equals("@")
				|| o2.getPinyin().equals("#")) {
			return -1;
		} else if (o1.getPinyin().equals("#")
				|| o2.getPinyin().equals("@")) {
			return 1;
		} else {
			return o1.getPinyin().compareTo(o2.getPinyin());
		}
	}

}
