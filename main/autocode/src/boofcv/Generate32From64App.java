/*
 * Copyright (c) 2011-2016, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Auto generates 32bit code from 64bit code.
 *
 * @author Peter Abeles
 */
public class Generate32From64App {

	// source code root directory
	File rootDirectory;

	public Generate32From64App( String sourceCodeRoot ) {
		rootDirectory = new File( sourceCodeRoot );

		if( !rootDirectory.isDirectory() ) {
			throw new IllegalArgumentException( "Must specify a directory" );
		}
	}


	public void process() {
		processDirectory( rootDirectory );
	}

	private void processDirectory( File directory ) {
		System.out.println( "---- Directory " + directory );

		// examine all the files in the directory first
		File[] files = directory.listFiles();

		for( File f : files ) {
			if( f.getName().endsWith( "_F64.java" ) ) {
				processFile( f );
			}
		}

		for( File f : files ) {
			if( f.isDirectory() && !f.isHidden() ) {
				processDirectory( f );
			}
		}
	}

	private void processFile( File f ) {
		try {
			System.out.println( "Examining " + f.getName() );
			new ConvertFile32From64( f ).process();
		} catch( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	public static String[] addTests( String[] original ) {
		List<String> path = new ArrayList<>();
		for( String a : original ) {
			path.add(a);
			if( a.contains("src"))
				path.add( a.replace("src","test"));
		}
		return path.toArray(new String[path.size()]);
	}

	public static void main( String args[] ) {
		String directories[] = new String[]{
				"main/geo/src/boofcv/alg/distort",
				"main/ip/src/boofcv/struct/distort"};

		directories = addTests(directories);

		for( String dir : directories ) {
			new Generate32From64App( dir ).process();
		}
	}
}
