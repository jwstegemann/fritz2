package dev.fritz2.headlessdemo.components

import dev.fritz2.core.*
import dev.fritz2.headless.components.combobox
import dev.fritz2.headless.foundation.utils.floatingui.core.middleware.offset
import dev.fritz2.headlessdemo.result
import kotlinx.coroutines.flow.map

fun RenderContext.comboboxDemo() {
    val selectionStore = storeOf<Country?>(null)
    val readOnlyStore = storeOf(false)
    val enableAutoselectStore = storeOf(false)

    div("max-w-96 flex flex-col gap-4") {
        combobox<Country>(id = "countries") {
            items(COUNTRY_LIST)
            itemFormat = Country::name
            value(selectionStore)
            filterBy(Country::name)

            enableAutoselectStore.data handledBy {
                if (it) selectionStrategy.autoSelectMatch()
                else selectionStrategy.manual()
            }

            maximumDisplayedItems = 20

            comboboxLabel("sr-only") {
                +"Select a Country"
            }

            comboboxPanelReference(
                joinClasses(
                    "w-full py-2.5 px-4 flex items-center bg-white rounded border border-primary-600",
                    "cursor-default font-sans text-sm text-left text-primary-800 hover:border-primary-800",
                    "focus-within:outline-none focus-within:ring-4 focus-within:ring-primary-600 focus-within:border-primary-800"
                ),
            ) {
                comboboxInput(
                    joinClasses(
                        "w-full block flex-grow bg-transparent border-0 outline-0 placeholder-gray-700",
                        "vui-label-4 p-0 focus:ring-transparent focus:shadow-none disabled:text-gray-400",
                        "text-ellipsis",
                    ),
                ) {
                    readOnly(readOnlyStore.data)
                }

                icon("pl-2w-5 h-5", content = HeroIcons.selector).clicks handledBy open
            }

            comboboxValidationMessages {
                ul("list-disc list-inside") {
                    msgs.render(into = this) { messages ->
                        messages.forEach { message ->
                            li {
                                +"(${message.severity}) ${message.message}"
                            }
                        }
                    }
                }
            }

            comboboxItems(
                joinClasses(
                    "max-h-60 py-1 overflow-auto origin-top z-30 bg-white rounded shadow-md divide-y divide-gray-100",
                    "ring-1 ring-primary-600 ring-opacity-5 focus:outline-none"
                )
            ) {
                addMiddleware(offset(5))

                transition(
                    opened,
                    enter = "transition duration-100 ease-out",
                    enterStart = "opacity-0 scale-95",
                    enterEnd = "opacity-100 scale-100",
                    leave = "transition duration-100 ease-in",
                    leaveStart = "opacity-100 scale-100",
                    leaveEnd = "opacity-0 scale-95"
                )

                results.render(into = this) { (query, results, truncated) ->
                    if (results.isEmpty()) {
                        p("px-4 py-2 text-gray-400") {
                            +"No results found"
                        }
                    } else {
                        results.forEach { item ->
                            comboboxItem(
                                item,
                                "w-full relative py-2 pl-10 pr-4 cursor-default select-none disabled:opacity-50 text-sm"
                            ) {
                                className(active.map { active->
                                    if (active) "bg-primary-600 text-white"
                                    else "text-primary-800"
                                })

                                highlightedText(item.value.name, query).run {
                                    className(selected.map {
                                        if (it) "font-semibold"
                                        else "font-normal"
                                    })
                                }

                                selected.render {
                                    if (it) {
                                        span("absolute left-0 inset-y-0 flex items-center pl-3") {
                                            icon("w-5 h-5", content = HeroIcons.check)
                                        }
                                    }
                                }
                            }
                        }
                        if (truncated) {
                            div("py-2 pl-10 pr-4 text-sm text-gray-400") {
                                +"Refine your query for more results"
                            }
                        }
                    }
                }
            }
        }

        div("space-y-2") {
            p("text-sm text-primary-800") {
                +"Select via data-binding:"
            }
            div("flex flex-wrap gap-2") {
                buildList {
                    addAll(COUNTRY_LIST.take(2))
                    addAll(COUNTRY_LIST.takeLast(2))
                }.forEach { country ->
                    quickSelectButton(country, selectionStore)
                }
            }
        }

        checkbox("Read-only", readOnlyStore, "checkbox-enable-readonly")
        checkbox("Auto-select exact matches (try 'oman')", enableAutoselectStore, "checkbox-enable-autoselect")

        result {
            p {
                span("font-semibold") { +"Selected: " }
                span(id = "countries-selection") {
                    selectionStore.data.renderText(into = this)
                }
            }
        }
    }
}

private fun RenderContext.highlightedText(text: String, highlight: String) =
    span {
        val split = text.split(Regex("(?<=($highlight))|(?=($highlight))", RegexOption.IGNORE_CASE))
        for (segment in split) {
            span("font-semibold".takeIf { segment.contentEquals(highlight, ignoreCase = true) }) {
                +segment
            }
        }
    }

private fun RenderContext.quickSelectButton(country: Country, store: Store<Country?>) {
    button("p-2 bg-primary-500 hover:bg-primary-600 shadow rounded text-sm text-primary-900") {
        +country.name
    }.clicks.map { country } handledBy store.update
}

private fun RenderContext.checkbox(text: String, value: Store<Boolean>, testId: String) {
    val id = Id.next()
    div("flex items-center gap-2") {
        input("", id = testId) {
            type("checkbox")
            checked(value.data)
        }.clicks.map { !value.current } handledBy value.update

        label {
            `for`(testId)
            +text
        }
    }
}

private data class Country(
    val code: String,
    val name: String,
)

private val COUNTRY_LIST = listOf(
    Country("AF", "Afghanistan"),
    Country("AX", "Åland Islands"),
    Country("AL", "Albania"),
    Country("DZ", "Algeria"),
    Country("AS", "American Samoa"),
    Country("AD", "Andorra"),
    Country("AO", "Angola"),
    Country("AI", "Anguilla"),
    Country("AQ", "Antarctica"),
    Country("AG", "Antigua and Barbuda"),
    Country("AR", "Argentina"),
    Country("AM", "Armenia"),
    Country("AW", "Aruba"),
    Country("AU", "Australia"),
    Country("AT", "Austria"),
    Country("AZ", "Azerbaijan"),
    Country("BS", "Bahamas"),
    Country("BH", "Bahrain"),
    Country("BD", "Bangladesh"),
    Country("BB", "Barbados"),
    Country("BY", "Belarus"),
    Country("BE", "Belgium"),
    Country("BZ", "Belize"),
    Country("BJ", "Benin"),
    Country("BM", "Bermuda"),
    Country("BT", "Bhutan"),
    Country("BOL", "Plurinational State of Bolivia"),
    Country("BES", "Sint Eustatius and Saba Bonaire"),
    Country("BA", "Bosnia and Herzegovina"),
    Country("BW", "Botswana"),
    Country("BV", "Bouvet Island"),
    Country("BR", "Brazil"),
    Country("IO", "British Indian Ocean Territory"),
    Country("BN", "Brunei Darussalam"),
    Country("BG", "Bulgaria"),
    Country("BF", "Burkina Faso"),
    Country("BI", "Burundi"),
    Country("CV", "Cabo Verde"),
    Country("KH", "Cambodia"),
    Country("CM", "Cameroon"),
    Country("CA", "Canada"),
    Country("KY", "Cayman Islands"),
    Country("CF", "Central African Republic"),
    Country("TD", "Chad"),
    Country("CL", "Chile"),
    Country("CN", "China"),
    Country("CX", "Christmas Island"),
    Country("CC", "Cocos (Keeling) Islands"),
    Country("CO", "Colombia"),
    Country("KM", "Comoros"),
    Country("CG", "Congo"),
    Country("COD", "Democratic Republic of the Congo"),
    Country("CK", "Cook Islands"),
    Country("CR", "Costa Rica"),
    Country("CI", "Côte d'Ivoire"),
    Country("HR", "Croatia"),
    Country("CU", "Cuba"),
    Country("CW", "Curaçao"),
    Country("CY", "Cyprus"),
    Country("CZ", "Czechia"),
    Country("DK", "Denmark"),
    Country("DJ", "Djibouti"),
    Country("DM", "Dominica"),
    Country("DO", "Dominican Republic"),
    Country("EC", "Ecuador"),
    Country("EG", "Egypt"),
    Country("SV", "El Salvador"),
    Country("GQ", "Equatorial Guinea"),
    Country("ER", "Eritrea"),
    Country("EE", "Estonia"),
    Country("SZ", "Eswatini"),
    Country("ET", "Ethiopia"),
    Country("FK", "Falkland Islands (Malvinas)"),
    Country("FO", "Faroe Islands"),
    Country("FJ", "Fiji"),
    Country("FI", "Finland"),
    Country("FR", "France"),
    Country("GF", "French Guiana"),
    Country("PF", "French Polynesia"),
    Country("TF", "French Southern Territories"),
    Country("GA", "Gabon"),
    Country("GM", "Gambia"),
    Country("GE", "Georgia"),
    Country("DE", "Germany"),
    Country("GH", "Ghana"),
    Country("GI", "Gibraltar"),
    Country("GR", "Greece"),
    Country("GL", "Greenland"),
    Country("GD", "Grenada"),
    Country("GP", "Guadeloupe"),
    Country("GU", "Guam"),
    Country("GT", "Guatemala"),
    Country("GG", "Guernsey"),
    Country("GN", "Guinea"),
    Country("GW", "Guinea-Bissau"),
    Country("GY", "Guyana"),
    Country("HT", "Haiti"),
    Country("HM", "Heard Island and McDonald Islands"),
    Country("VA", "Holy See"),
    Country("HN", "Honduras"),
    Country("HK", "Hong Kong"),
    Country("HU", "Hungary"),
    Country("IS", "Iceland"),
    Country("IN", "India"),
    Country("ID", "Indonesia"),
    Country("IRN", "Islamic Republic of IranIR"),
    Country("IQ", "Iraq"),
    Country("IE", "Ireland"),
    Country("IM", "Isle of Man"),
    Country("IL", "Israel"),
    Country("IT", "Italy"),
    Country("JM", "Jamaica"),
    Country("JP", "Japan"),
    Country("JE", "Jersey"),
    Country("JO", "Jordan"),
    Country("KZ", "Kazakhstan"),
    Country("KE", "Kenya"),
    Country("KI", "Kiribati"),
    Country("PRK", "Democratic People's Republic of Korea"),
    Country("KOR", "Republic of Korea"),
    Country("KW", "Kuwait"),
    Country("KG", "Kyrgyzstan"),
    Country("LA", "Lao People's Democratic Republic"),
    Country("LV", "Latvia"),
    Country("LB", "Lebanon"),
    Country("LS", "Lesotho"),
    Country("LR", "Liberia"),
    Country("LY", "Libya"),
    Country("LI", "Liechtenstein"),
    Country("LT", "Lithuania"),
    Country("LU", "Luxembourg"),
    Country("MO", "Macao"),
    Country("MG", "Madagascar"),
    Country("MW", "Malawi"),
    Country("MY", "Malaysia"),
    Country("MV", "Maldives"),
    Country("ML", "Mali"),
    Country("MT", "Malta"),
    Country("MH", "Marshall Islands"),
    Country("MQ", "Martinique"),
    Country("MR", "Mauritania"),
    Country("MU", "Mauritius"),
    Country("YT", "Mayotte"),
    Country("MX", "Mexico"),
    Country("FSM", "Federated States of Micronesia"),
    Country("MDA", "Republic of Moldova"),
    Country("MC", "Monaco"),
    Country("MN", "Mongolia"),
    Country("ME", "Montenegro"),
    Country("MS", "Montserrat"),
    Country("MA", "Morocco"),
    Country("MZ", "Mozambique"),
    Country("MM", "Myanmar"),
    Country("NA", "Namibia"),
    Country("NR", "Nauru"),
    Country("NP", "Nepal"),
    Country("NLD", "Kingdom of the Netherlands"),
    Country("NC", "New Caledonia"),
    Country("NZ", "New Zealand"),
    Country("NI", "Nicaragua"),
    Country("NE", "Niger"),
    Country("NG", "Nigeria"),
    Country("NU", "Niue"),
    Country("NF", "Norfolk Island"),
    Country("MK", "North Macedonia"),
    Country("MP", "Northern Mariana Islands"),
    Country("NO", "Norway"),
    Country("OM", "Oman"),
    Country("PK", "Pakistan"),
    Country("PW", "Palau"),
    Country("PSE", "State of Palestine"),
    Country("PA", "Panama"),
    Country("PG", "Papua New Guinea"),
    Country("PY", "Paraguay"),
    Country("PE", "Peru"),
    Country("PH", "Philippines"),
    Country("PN", "Pitcairn"),
    Country("PL", "Poland"),
    Country("PT", "Portugal"),
    Country("PR", "Puerto Rico"),
    Country("QA", "Qatar"),
    Country("RE", "Réunion"),
    Country("RO", "Romania"),
    Country("RU", "Russian Federation"),
    Country("RW", "Rwanda"),
    Country("BL", "Saint Barthélemy"),
    Country("SHN", "Ascension and Tristan da Cunha Saint Helena"),
    Country("KN", "Saint Kitts and Nevis"),
    Country("LC", "Saint Lucia"),
    Country("MF", "Saint Martin (French part)"),
    Country("PM", "Saint Pierre and Miquelon"),
    Country("VC", "Saint Vincent and the Grenadines"),
    Country("WS", "Samoa"),
    Country("SM", "San Marino"),
    Country("ST", "Sao Tome and Principe"),
    Country("SA", "Saudi Arabia"),
    Country("SN", "Senegal"),
    Country("RS", "Serbia"),
    Country("SC", "Seychelles"),
    Country("SL", "Sierra Leone"),
    Country("SG", "Singapore"),
    Country("SX", "Sint Maarten (Dutch part)"),
    Country("SK", "Slovakia"),
    Country("SI", "Slovenia"),
    Country("SB", "Solomon Islands"),
    Country("SO", "Somalia"),
    Country("ZA", "South Africa"),
    Country("GS", "South Georgia and the South Sandwich Islands"),
    Country("SS", "South Sudan"),
    Country("ES", "Spain"),
    Country("LK", "Sri Lanka"),
    Country("SD", "Sudan"),
    Country("SR", "Suriname"),
    Country("SJ", "Svalbard and Jan Mayen"),
    Country("SE", "Sweden"),
    Country("CH", "Switzerland"),
    Country("SY", "Syrian Arab Republic"),
    Country("TWN", "Taiwan"),
    Country("TJ", "Tajikistan"),
    Country("TZA", "United Republic of Tanzania"),
    Country("TH", "Thailand"),
    Country("TL", "Timor-Leste"),
    Country("TG", "Togo"),
    Country("TK", "Tokelau"),
    Country("TO", "Tonga"),
    Country("TT", "Trinidad and Tobago"),
    Country("TN", "Tunisia"),
    Country("TR", "Türkiye"),
    Country("TM", "Turkmenistan"),
    Country("TC", "Turks and Caicos Islands"),
    Country("TV", "Tuvalu"),
    Country("UG", "Uganda"),
    Country("UA", "Ukraine"),
    Country("AE", "United Arab Emirates"),
    Country("GB", "United Kingdom of Great Britain and Northern Ireland"),
    Country("US", "United States of America"),
    Country("UM", "United States Minor Outlying Islands"),
    Country("UY", "Uruguay"),
    Country("UZ", "Uzbekistan"),
    Country("VU", "Vanuatu"),
    Country("VEN", "Bolivarian Republic of Venezuela"),
    Country("VN", "Viet Nam"),
    Country("VG", "Virgin Islands (British)"),
    Country("VI", "Virgin Islands (U.S.)"),
    Country("WF", "Wallis and Futuna"),
    Country("EH", "Western Sahara"),
    Country("YE", "Yemen"),
    Country("ZM", "Zambia"),
    Country("ZW", "Zimbabwe"),
)