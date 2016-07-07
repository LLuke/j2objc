/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.devtools.j2objc.ast;

import com.google.devtools.j2objc.javac.BindingConverter;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Abstract base class of all AST node types that represent a method reference expression (added in
 * JLS8, section 15.13).
 *
 * <pre>
 * MethodReference:
 *    CreationReference
 *    ExpressionMethodReference
 *    SuperMethodReference
 *    TypeMethodReference
 * </pre>
 */
public abstract class MethodReference extends Expression {

  protected TypeMirror typeMirror;
  protected IMethodBinding methodBinding;
  protected ChildList<Type> typeArguments = ChildList.create(Type.class, this);
  // We generate an invocation to properly resolve translations with normal visitors.
  protected ChildLink<Statement> invocation = ChildLink.create(Statement.class, this);

  public MethodReference(org.eclipse.jdt.core.dom.MethodReference jdtNode) {
    super(jdtNode);
    ITypeBinding typeBinding = BindingConverter.wrapBinding(jdtNode.resolveTypeBinding());
    typeMirror = BindingConverter.getType(typeBinding);
    methodBinding = BindingConverter.wrapBinding(jdtNode.resolveMethodBinding());
    for (Object x : jdtNode.typeArguments()) {
      typeArguments.add((Type) TreeConverter.convert(x));
    }
  }

  public MethodReference(MethodReference other) {
    super(other);
    typeMirror = other.getTypeMirror();
    methodBinding = other.getMethodBinding();
    typeArguments.copyFrom(other.getTypeArguments());
    invocation.copyFrom(other.getInvocation());
  }

  @Override
  public TypeMirror getTypeMirror() {
    return typeMirror;
  }

  public IMethodBinding getMethodBinding() {
    return methodBinding;
  }

  public List<Type> getTypeArguments() {
    return typeArguments;
  }

  public Statement getInvocation() {
    return invocation.get();
  }

  public void setInvocation(Statement invocation) {
    this.invocation.set(invocation);
  }

  @Override
  public abstract MethodReference copy();
}
