// SPDX-License-Identifier: Apache-2.0
package testsoc
import firrtl.transforms.formal._

import firrtl.ir.{Circuit, Formal, Statement, Verification}
import firrtl.stage.TransformManager.TransformDependency
import firrtl.{CircuitState, DependencyAPIMigration, Transform}
import firrtl.annotations.NoTargetAnnotation
import firrtl.options.{PreservesAll, RegisteredTransform, ShellOption}

/**
  * Assert Submodule Assumptions
  *
  * Converts `assume` statements to `assert` statements in all modules except
  * the top module being compiled. This avoids a class of bugs in which an
  * overly restrictive assume in a child module can prevent the model checker
  * from searching valid inputs and states in the parent module.
  */
class KeepSubmoduleAssumptions
    extends Transform
    with RegisteredTransform
    with DependencyAPIMigration
    with PreservesAll[Transform] {

  override def prerequisites:         Seq[TransformDependency] = Seq.empty
  override def optionalPrerequisites: Seq[TransformDependency] = Seq.empty
  override def optionalPrerequisiteOf: Seq[TransformDependency] = firrtl.stage.Forms.MidEmitters

  val options = Seq()

  def keepAssumption(s: Statement): Statement = s match {
    case v: Verification if v.op == Formal.Assume => v.withOp(Formal.Assume)
    case t => t.mapStmt(keepAssumption)
  }

  def run(c: Circuit): Circuit = {
    c.mapModule(mod => {
      if (mod.name != c.main) {
        mod.mapStmt(keepAssumption)
      } else {
        mod
      }
    })
  }

  def execute(state: CircuitState): CircuitState = {
    state.copy(circuit = run(state.circuit))
  }
}

case object KeepSubmoduleAssumptionsAnnotation extends NoTargetAnnotation {
  val transform = new KeepSubmoduleAssumptions
}