<#if nativePackageName?has_content>
package ${nativePackageName};

</#if>
import java.util.Map;

<#assign actionStateName = translator.actionStateName(element) />
public class ${nativeClassName} extends ${translator.nativeParentFlowNode(element)} implements ${translator.nativeLGNode(element)}<${actionStateName}> {
    public ${nativeClassName}() {
        super("${translator.escapeInString(element.name)}");
    }

    <#if translator.isDivergent(element) >
    @Override
    public String apply(${actionStateName} state) throws Exception {
        <#assign branches = translator.branches(element)>
        <#list branches as branch>
        if (elCondition(
            """
            ${branch.condition()}
            """
            , state.data())) {
            return "${branch.target()}";
        }
        </#list>
        <#assign defaultBranch = translator.defaultBranch(element)! >
        <#if defaultBranch?has_content>
        return "${defaultBranch.target()}";
        <#else>
        return "";
        </#if>

    }
    <#else>
    @Override
    public Map<String, Object> apply(${actionStateName} state) throws Exception {
        return execute(state.data(), state.getContext());
    }
    </#if>
}
