package com.prj.tuning.maplocator.export.ecuvars.model;

import java.util.Collection;
import java.util.HashSet;

import com.prj.tuning.maplocator.model.LocatedMap;

public class ECUVariablesProject extends ECUVariables {
	private Collection<ECUVariable> ecuVar = new HashSet<ECUVariable>();

	public ECUVariablesProject(Collection<LocatedMap> locatedMaps) {
		for (LocatedMap locatedMap : locatedMaps) {
			if (locatedMap.isEcuvar())
				ecuVar.add(ecuvarFromLocatedMap(locatedMap));
		}
	}

	@Override
	public Collection<ECUVariable> getEcuvars() {
		return ecuVar;
	}

	public static ECUVariable ecuvarFromLocatedMap(LocatedMap l) {
		return new ECUVariable(l.getId(), l.getDesc(), Integer.toHexString(l
				.getAddress()), l.getUnits(), l.getWidth() == 2 ? true : false,
				l.isSigned(), l.getFactor(), l.getOffset(), l.getPrecision());
	}
}
