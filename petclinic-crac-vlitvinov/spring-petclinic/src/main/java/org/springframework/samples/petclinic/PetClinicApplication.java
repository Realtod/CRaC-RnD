/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportRuntimeHints;

import org.crac.Core;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@EnableCaching
@ImportRuntimeHints(PetClinicRuntimeHints.class)
public class PetClinicApplication {

	public static void main(String[] args) throws Exception {
		boolean makeEarlyCheckpoint = Boolean.parseBoolean(System.getenv("MAKE_EARLY_CHECKPOINT"));
		if (makeEarlyCheckpoint) {
			try {
				Core.checkpointRestore();
				// Если дошли сюда — чекпоинт НЕ поддерживается, завершаем:
				System.exit(0);
			}
			catch (org.crac.RestoreException re) {
				// Это restore! Просто продолжаем выполнение (идем дальше)
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		SpringApplication.run(PetClinicApplication.class, args);
	}

}
