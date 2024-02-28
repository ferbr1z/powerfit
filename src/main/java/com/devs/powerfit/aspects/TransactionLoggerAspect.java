package com.devs.powerfit.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Stack;

@Aspect
@Component
public class TransactionLoggerAspect {
    private Logger logger = LoggerFactory.getLogger(TransactionLoggerAspect.class);

    private ThreadLocal<Stack<TransactionStatus>> transactionStack = ThreadLocal.withInitial(Stack::new);

    @Before("@annotation(transactional) && execution(* *.*(..))")
    public void logTransactionStart(JoinPoint joinPoint, Transactional transactional) {
        String methodName = joinPoint.getSignature().getName();

        try {
            if (isTransactionActive()) {
                logger.info("Utilizando transacción existente en el método: " + methodName);
            } else {
                checkTransactionPropagation(transactional, methodName);
                if (shouldLogTransactionStart(transactional)) {
                    logger.info("Inicia transacción en el método: " + methodName);
                }
            }
        } catch (IllegalTransactionStateException e) {
            logger.warn(e.getMessage());
        }
    }

    @AfterReturning("@annotation(transactional) && execution(* *.*(..))")
    public void logTransactionCommit(JoinPoint joinPoint, Transactional transactional) {
        String methodName = joinPoint.getSignature().getName();

        if (isTransactionNested()) {
            if (shouldLogTransactionCommit(transactional)) {
                logger.info("Finalizó la tarea en el método: " + methodName);
                logger.info("Realizando commit en la transacción anidada.");
                TransactionStatus currentTransactionStatus = transactionStack.get().pop();
                currentTransactionStatus.flush();
            }
        } else {
            if (shouldLogTransactionCommit(transactional)) {
                logger.info("Commit en el método: " + methodName);
            }
            transactionStack.get().clear();
        }
    }

    @AfterThrowing(pointcut = "@annotation(transactional) && execution(* *.*(..))", throwing = "ex")
    public void logTransactionRollback(JoinPoint joinPoint, Transactional transactional, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Rollback en el método: ").append(methodName);
        logMessage.append(" debido a: ").append(ex.getMessage());
        if (args != null && args.length > 0) {
            logMessage.append(" con parámetros: ");
            for (Object arg : args) {
                logMessage.append(arg).append(", ");
            }
            logMessage.setLength(logMessage.length() - 2);
        }

        logger.warn(logMessage.toString());
    }

    private void checkTransactionPropagation(Transactional transactional, String methodName) {
        if (transactional != null
                && transactional.propagation().equals(TransactionDefinition.PROPAGATION_MANDATORY)
                && !isTransactionActive()) {
            logger.warn("Intento de ejecutar método MANDATORY sin una transacción existente en el método: " + methodName);
            throw new IllegalTransactionStateException("Intento de iniciar transacción MANDATORY sin una transacción existente en el método: " + methodName);
        }

        if (transactional != null
                && transactional.propagation().equals(TransactionDefinition.PROPAGATION_NEVER)
                && !isTransactionActive()) {
        } else {
            TransactionStatus currentTransactionStatus = TransactionAspectSupport.currentTransactionStatus();
            transactionStack.get().push(currentTransactionStatus);
        }
    }

    private boolean isTransactionActive() {
        return !transactionStack.get().isEmpty();
    }

    private boolean isTransactionNested() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);

        TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
        return status != null && status.isNewTransaction();
    }

    private boolean shouldLogTransactionStart(Transactional transactional) {
        return !transactional.propagation().equals(TransactionDefinition.PROPAGATION_NEVER)
                && isTransactionActive();
    }

    private boolean shouldLogTransactionCommit(Transactional transactional) {
        return !transactional.propagation().equals(TransactionDefinition.PROPAGATION_NEVER)
                && isTransactionActive();
    }
}

