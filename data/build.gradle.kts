@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinJvm)
    kotlin("kapt")
}

dependencies {
    implementation(project(":utils"))
    implementation(project(":domain"))

    implementation(libs.dagger.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.paging.common)

    implementation(libs.arrow.core)
    implementation(libs.arrow.core.retrofit)
}