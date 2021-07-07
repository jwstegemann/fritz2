package dev.fritz2.components.forms

/**
 * Marker CSS class name for all container elements of a single formControl rendering. The ControlRenderer
 * implementation must apply this to all container elements until the base level, where the label and the control
 * itself are exposed.
 *
 * This ensures that the label and the rest of the control fit into a 2 column grid if the label is placed left hand
 * side.
 *
 * For example:
 * ```
 * <div class="formGroupElementContainerMarker"> // must be excluded from layout
 *     <fieldset class="formGroupElementContainerMarker"> // must be excluded from layout
 *         <label></label>
 *         <div>
 *             <input ...></input>
 *             <!-- Helpertext -->
 *             <!-- Errormessages -->
 *         </div>
 *     </fieldset>
 * </div>
 * ```
 */
const val formGroupElementContainerMarker = "formGroupElementContainerMarker"

/**
 * Should be applied by ControlRenderer implementations to the label element, if its control is "one single line"
 * like an inputField or a selectField.
 */
const val formGroupElementLabelMarker = "formGroupElementLabelMarker"

/**
 * Should be applied by ControlRenderer implementations to the legend element if its control renders a group of
 * options like a checkboxGroup.
 */
const val formGroupElementLegendMarker = "formGroupElementLegendMarker"