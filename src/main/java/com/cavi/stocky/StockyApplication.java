package com.cavi.stocky;


import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// clase principal del proyecto, desde aca arranca todo
@SpringBootApplication // le dice a spring que escanee y configure toda la aplicacion
@Aspect // habilita Aop (programacion orientada a aspectos)
public class StockyApplication {
	//se inicia el logger
	public static final Logger log = LoggerFactory.getLogger(StockyApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(StockyApplication.class, args);
	}// enciende el servidor

	//se definen carpetas a usar
	@Pointcut("within(com.cavi.stocky.controller..*) || within(com.cavi.stocky.exception..*) || within(com.cavi.stocky.resource..*)")
	public void applicationPackagePointcut(){}

	//se interceptan metodos para entradas, salidas y errores
	@Around("applicationPackagePointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable{
		String clase = joinPoint.getSignature().getDeclaringTypeName();
		String metodo = joinPoint.getSignature().getName();

		//log antes de que se ejecute el metodo
		log.info("Entrando a: {}.{}() | Argumentos: {}", clase, metodo, Arrays.toString(joinPoint.getArgs()));
			try{
				//ejecuta metodo original de forma intacta
				Object resultado = joinPoint.proceed();

				//log si el metodo es exitoso
				log.info("Saliendo de: {}.{}() | Resultado: {}", clase, metodo, resultado);
				return resultado;
			}catch(Throwable e){
				//log si el metodo lanza excepcion
				log.error("Error en: {}.{}() | Excepcion: {}",clase, metodo, e.getMessage());
				throw e; //lanza error original 
			}
	}
}
