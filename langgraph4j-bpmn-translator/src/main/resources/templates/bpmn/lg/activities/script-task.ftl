<#if nativePackageName?has_content>
package ${nativePackageName};

</#if>
import java.util.Map;

<#assign actionStateName = translator.actionStateName(element) />
public class ${nativeClassName} extends ${translator.nativeParentFlowNode(element)} implements ${translator.nativeLGNode(element)}<${actionStateName}> {
    public ${nativeClassName}() {
        super("${translator.escapeInString(element.name)}");
        setScript(
            """
            ${translator.getScript(element)}
            """
        );
        <#assign resultVariable = translator.getResultVariable(element)! />
        <#if resultVariable?has_content>
        setResultVariable("${resultVariable}");
        </#if>
    }

    @Override
    public Map<String, Object> apply(${actionStateName} state) throws Exception {
        return execute(state.data(), state.getContext());
    }
}
