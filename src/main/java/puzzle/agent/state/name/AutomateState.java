package puzzle.agent.state.name;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Garder les annotations lors du runtine.
@Target(ElementType.TYPE) // Pouvoir annoter les classes.
public @interface AutomateState {

    StateName name() default StateName.NONE;

}
