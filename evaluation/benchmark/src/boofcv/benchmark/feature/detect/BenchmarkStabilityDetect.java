/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://www.boofcv.org).
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

package boofcv.benchmark.feature.detect;

import boofcv.benchmark.feature.distort.BenchmarkFeatureDistort;
import boofcv.benchmark.feature.distort.CompileImageResults;
import boofcv.benchmark.feature.distort.FactoryBenchmarkFeatureDistort;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;


/**
 * @author Peter Abeles
 */
public class BenchmarkStabilityDetect<T extends ImageBase, D extends ImageBase> {

	Class<T> imageType;
	Class<D> derivType;

	public BenchmarkStabilityDetect(Class<T> imageType, Class<D> derivType) {
		this.imageType = imageType;
		this.derivType = derivType;
	}

	public void testNoise() {
		BenchmarkFeatureDistort<T> benchmark =
				FactoryBenchmarkFeatureDistort.noise(imageType);
		perform(benchmark);
	}

	public void testIntensity() {
		BenchmarkFeatureDistort<T> benchmark =
				FactoryBenchmarkFeatureDistort.intensity(imageType);
		perform(benchmark);
	}

	public void testRotation() {
		BenchmarkFeatureDistort<T> benchmark =
				FactoryBenchmarkFeatureDistort.rotate(imageType);
		perform(benchmark);
	}

	public void testScale() {
		BenchmarkFeatureDistort<T> benchmark =
				FactoryBenchmarkFeatureDistort.scale(imageType);
		perform(benchmark);
	}

	private void perform( BenchmarkFeatureDistort<T> benchmark ) {
		BenchmarkInterestParameters<T,D> param = new BenchmarkInterestParameters<T,D>();
		param.imageType = imageType;
		param.derivType = derivType;

		CompileImageResults<T> compile = new CompileImageResults<T>(benchmark);
		compile.addImage("evaluation/data/outdoors01.jpg");
		compile.addImage("evaluation/data/indoors01.jpg");
		compile.addImage("evaluation/data/scale/beach01.jpg");
		compile.addImage("evaluation/data/scale/mountain_7p1mm.jpg");
		compile.addImage("evaluation/data/sunflowers.png");

		DetectEvaluator<T> evaluator = new DetectEvaluator<T>();

		compile.setAlgorithms(BenchmarkDetectHelper.createAlgs(param),evaluator);

		compile.process();

	}

	public static void main( String args[] ) {
		BenchmarkStabilityDetect<ImageFloat32,ImageFloat32> benchmark
				= new BenchmarkStabilityDetect<ImageFloat32,ImageFloat32>(ImageFloat32.class,ImageFloat32.class);

		benchmark.testNoise();
//		benchmark.testIntensity();
//		benchmark.testRotation();
//		benchmark.testScale();

	}
}