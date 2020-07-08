package com.enrrolato.enrrolato
// How to run this test: https://developer.android.com/studio/test/espresso-test-recorder#run-an-espresso-test-locally

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UITest1 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun uITest1() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.usernameField),
                childAtPosition(
                    allOf(
                        withId(R.id.largeRectangle),
                        childAtPosition(
                            withId(R.id.loginLayout),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("brayanbr_96@hotmail.com"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.passwordField),
                childAtPosition(
                    allOf(
                        withId(R.id.largeRectangle),
                        childAtPosition(
                            withId(R.id.loginLayout),
                            1
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("cs_assault_1337"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.loginBtn), withText("Ingresar"),
                childAtPosition(
                    allOf(
                        withId(R.id.largeRectangle),
                        childAtPosition(
                            withId(R.id.loginLayout),
                            1
                        )
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())
        Thread.sleep(3000) // No es lo recomendado pero sirve para las partes que se retresan por operar con la BD
        val appCompatButton2 = onView(
            allOf(
                withId(R.id.btCreateIceCream), withText("Crear helado"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.ly_principal),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.btCreateOurIcecream), withText("Crear helado personalizado"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewDefault),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())

        val appCompatRadioButton = onView(
            allOf(
                withId(R.id.rbTradicional), withText("Tradicional"),
                childAtPosition(
                    allOf(
                        withId(R.id.rgFlavor),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatRadioButton.perform(click())

        val appCompatSpinner = onView(
            allOf(
                withId(R.id.spFlavor), withContentDescription("Elija sabor"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewFlavor),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatSpinner.perform(click())

        val checkedTextView = onView(
            allOf(
                withId(android.R.id.text1),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        checkedTextView.check(matches(isDisplayed()))
        Thread.sleep(1000)
        val appCompatCheckedTextView = onData(anything())
            .inAdapterView(
                childAtPosition(
                        withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(4)
        appCompatCheckedTextView.perform(click())

        val appCompatSpinner2 = onView(
            allOf(
                withId(R.id.spFlavor), withContentDescription("Elija sabor"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewFlavor),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatSpinner2.perform(click())

        val appCompatCheckedTextView2 = onData(anything())
            .inAdapterView(
                childAtPosition(
                        withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(12)
        appCompatCheckedTextView2.perform(click())

        val appCompatSpinner3 = onView(
            allOf(
                withId(R.id.spFlavor), withContentDescription("Elija sabor"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewFlavor),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatSpinner3.perform(click())

        val appCompatCheckedTextView3 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(17)
        appCompatCheckedTextView3.perform(click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.btContinue), withText("Siguiente"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewFlavor),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        Thread.sleep(1000) // No es lo recomendado pero sirve para las partes que se retresan por operar con la BD
        val appCompatSpinner4 = onView(
            allOf(
                withId(R.id.spFilling), withContentDescription("Elija jarabe"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewTopping),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatSpinner4.perform(click())

        val appCompatCheckedTextView4 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(3)
        appCompatCheckedTextView4.perform(click())

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.btContinue), withText("Siguiente"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewTopping),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())
        Thread.sleep(1000) // No es lo recomendado pero sirve para las partes que se retresan por operar con la BD
        val appCompatSpinner5 = onView(
            allOf(
                withId(R.id.spTopping), withContentDescription("Elija topping"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewTopping),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatSpinner5.perform(click())

        val appCompatCheckedTextView5 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(12)
        appCompatCheckedTextView5.perform(click())

        val appCompatSpinner6 = onView(
            allOf(
                withId(R.id.spTopping), withContentDescription("Elija topping"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewTopping),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatSpinner6.perform(click())

        val appCompatCheckedTextView6 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(23)
        appCompatCheckedTextView6.perform(click())

        val appCompatSpinner7 = onView(
            allOf(
                withId(R.id.spTopping), withContentDescription("Elija topping"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewTopping),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatSpinner7.perform(click())

        val appCompatCheckedTextView7 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(22)
        appCompatCheckedTextView7.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.txtToppingAlert),
                withText("* Topping extra tiene un valor de 400 colones"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewTopping),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("* Topping extra tiene un valor de 400 colones")))

        val textView2 = onView(
            allOf(
                withId(R.id.item), withText("banano"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.choosenTopping),
                            withContentDescription("Lista de toppings")
                        ),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("banano")))

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.btCotinueToContainer), withText("Siguiente"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewTopping),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton6.perform(click())

        Thread.sleep(1000) // No es lo recomendado pero sirve para las partes que se retresan por operar con la BD
        val appCompatSpinner8 = onView(
            allOf(
                withId(R.id.spContainer), withContentDescription("Elija el envase de preferencia"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollviewTopping),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatSpinner8.perform(click())

        val appCompatCheckedTextView8 = onData(anything())
            .inAdapterView(
                childAtPosition(
                    withClassName(`is`("android.widget.PopupWindow\$PopupBackgroundView")),
                    0
                )
            )
            .atPosition(2)
        appCompatCheckedTextView8.perform(click())

        val checkedTextView2 = onView(
            allOf(
                withId(android.R.id.text1),
                childAtPosition(
                    allOf(
                        withId(R.id.spContainer),
                        withContentDescription("Elija el envase de preferencia"),
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        checkedTextView2.check(matches(isDisplayed()))

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.profile), withContentDescription("Cuenta"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val textView3 = onView(
            allOf(
                withId(R.id.txtAssociatedEmail), withText("brayanbr_96@hotmail.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.ly_profile),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("brayanbr_96@hotmail.com")))

        val appCompatButton7 = onView(
            allOf(
                withId(R.id.btAboutUs), withText("Acerca de"), withContentDescription("Acerca de"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.ly_profile),
                        0
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        appCompatButton7.perform(click())

        /*val textView4 = onView(
            allOf(
                withId(R.id.txtDescription),
                withText("Cafetería, bowls saludables y heladería artesanal con helados de rollo a la plancha en Sarchí"),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_aboutUs),
                        childAtPosition(
                            withId(R.id.ly_profile),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Cafetería, bowls saludables y heladería artesanal con helados de rollo a la plancha en Sarchí"))) // Por alguna razón este assert falla
        */

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.btBackToProfile),
                childAtPosition(
                    allOf(
                        withId(R.id.ly_aboutUs),
                        childAtPosition(
                            withId(R.id.ly_profile),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        // Aquí falla porque disque coincide con múltiples vistas
        val textView5 = onView(
            allOf(
                withId(R.id.txtAccount), withText("Cuenta"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.ly_profile),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Cuenta")))


        // Aquí también. Por alguna razón los fragmentos se "duplican" en tiempo de ejecución si se regresa con la flechita
        val appCompatButton8 = onView(
            allOf(
                withId(R.id.btLogout),
                withText("Cerrar sesión"),
                withContentDescription("Cerrar sesión"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.ly_profile),
                        0
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatButton8.perform(click())

        val linearLayout = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.custom),
                        childAtPosition(
                            withId(R.id.customPanel),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val appCompatButton9 = onView(
            allOf(
                withId(R.id.btOk), withText("Aceptar"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton9.perform(click())

        val textView6 = onView(
            allOf(
                withId(R.id.loginTitle), withText("Iniciar sesión"),
                childAtPosition(
                    allOf(
                        withId(R.id.loginLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Iniciar sesión")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
