/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright (c) 2021
 */

package net.nprod.wikidataLotusExporter.lotus.models

data class Reference(
    val wikidataId: String,
    val dois: List<String>,
    val title: String?
)
