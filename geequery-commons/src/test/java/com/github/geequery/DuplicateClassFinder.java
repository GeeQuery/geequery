package com.github.geequery;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.geequery.tools.IOUtils;

public class DuplicateClassFinder {
	@Test
	public void run(){
		File root=new File("G:/Git/geequery/geequery-orm/src/main");
		Map<String,File> map=new HashMap<String,File>();
		work(root,map);
		
		work(new File("G:/Git/geequery/geequery-core/src/main"),map);
		
		
		
	}

	private void work(File root, Map<String, File> map) {
		for(File file:IOUtils.listFilesRecursive(root, "java")){
			String name=IOUtils.removeExt(file.getName());
			File old=map.put(name, file);
			if(old!=null){
				System.out.println(name);
				System.out.println(file.getAbsolutePath());
				System.out.println(old.getAbsolutePath());
				System.out.println();
			}
		}
	}
	

}
