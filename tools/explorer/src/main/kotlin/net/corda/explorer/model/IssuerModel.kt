package net.corda.explorer.model

import javafx.collections.ObservableList
import kotlinx.support.jdk8.collections.removeIf
import net.corda.client.fxutils.foldToObservableList
import net.corda.client.model.NodeMonitorModel
import net.corda.client.model.observable
import net.corda.core.contracts.currency
import net.corda.core.node.NodeInfo
import net.corda.core.node.services.NetworkMapCache
import tornadofx.observable
import java.util.*

val ISSUER_SERVICE_TYPE = Regex("corda.issuer.(USD|GBP|CHF)")

class IssuerModel {

    private val networkIdentityObservable by observable(NodeMonitorModel::networkMap)

    val networkIdentities: ObservableList<NodeInfo> =
            networkIdentityObservable.foldToObservableList(Unit) { update, _accumulator, observableList ->
                observableList.removeIf {
                    when (update) {
                        is NetworkMapCache.MapChange.Removed -> it == update.node
                        is NetworkMapCache.MapChange.Modified -> it == update.previousNode
                        else -> false
                    }
                }
                observableList.addAll(update.node)
            }

    fun advertisedServicesOfType(serviceName: Regex): ObservableList<NodeInfo> = networkIdentities.filtered { it.advertisedServices.any { it.info.type.id.matches(serviceName) } }

    val issuers: ObservableList<NodeInfo> = advertisedServicesOfType(ISSUER_SERVICE_TYPE)
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