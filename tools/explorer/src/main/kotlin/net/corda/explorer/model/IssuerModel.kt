package net.corda.explorer.model

import javafx.collections.ObservableList
import net.corda.core.contracts.currency
import net.corda.core.node.NodeInfo
import tornadofx.observable
import java.util.*

val ISSUER_SERVICE_TYPE = Regex("corda.issuer.(USD|GBP|CHF)")

class IssuerModel {
}

fun isIssuerNode(myIdentity: NodeInfo?): Boolean {
    myIdentity?.let { myIdentity ->
        if (myIdentity.advertisedServices.any { it.info.type.id.matches(ISSUER_SERVICE_TYPE) }) {
            return true
        }
    }
    return false
}

fun issuerCurrency(myIdentity: NodeInfo?): Currency? {
    myIdentity?.let { myIdentity ->
        if (isIssuerNode(myIdentity)) {
            val issuer = myIdentity.advertisedServices.first { it.info.type.id.matches(ISSUER_SERVICE_TYPE) }
            return currency(issuer.info.type.id.substringAfterLast("."))
        }
    }
    return null
}

fun transactionTypes(myIdentity: NodeInfo?) : ObservableList<CashTransaction> {
    if (isIssuerNode(myIdentity))
        return CashTransaction.values().asList().observable()
    else
        return listOf(CashTransaction.Pay).observable()
}

fun currencyTypes(myIdentity: NodeInfo?) : ObservableList<Currency> {
    issuerCurrency(myIdentity)?.let {
        return (listOf(it)).observable()
    } ?:
        return ReportingCurrencyModel().supportedCurrencies
}