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

package boofcv.alg.distort.spherical;

import boofcv.struct.calib.CameraPinhole;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestEquirectangularToPinhole_F32 {

	private int equiWidth = 600;
	private int equiHeight = 400;

	private int imgWidth = 300;
	private int imgHeight = 240;

	/**
	 * Makes sure the canonical orientation is pointed along the positive z axis.  This is done by projecting
	 * the center of the pinhole at default orientation.
	 */
	@Test
	public void canonicalIsPointedPositiveZ() {
		CameraPinhole intrinsic = new CameraPinhole(400,400,0,imgWidth/2,imgHeight/2,imgWidth,imgHeight);

		EquirectangularToPinhole_F32 alg = new EquirectangularToPinhole_F32();

		alg.setPinhole(intrinsic);
		alg.setEquirectangularShape(equiWidth,equiHeight);

		assertPointing(alg,imgWidth/2,imgHeight/2,0,0,1);
	}

	/**
	 * Rotate the camera and see if the camera center is pointing in the right direction now
	 */
	@Test
	public void setDirection() {
		CameraPinhole intrinsic = new CameraPinhole(400,400,0,imgWidth/2,imgHeight/2,imgWidth,imgHeight);

		EquirectangularToPinhole_F32 alg = new EquirectangularToPinhole_F32();

		alg.setPinhole(intrinsic);
		alg.setEquirectangularShape(equiWidth,equiHeight);
		alg.setDirection(0, (float)Math.PI/2, 0);

		assertPointing(alg,imgWidth/2,imgHeight/2,1,0,0);
	}

	private void assertPointing( EquirectangularToPinhole_F32 alg , int x , int y , float nx , float ny , float nz )
	{
		EquirectangularTools_F32 tools = new EquirectangularTools_F32();

		tools.configure(equiWidth,equiHeight);

		Point3D_F32 n = new Point3D_F32();

		alg.compute(x,y);
		tools.equiToNormFV(alg.distX,alg.distY,n);

		assertEquals( nx, n.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( ny, n.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals( nz, n.z, GrlConstants.FLOAT_TEST_TOL);
	}
}