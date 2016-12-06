Node Explorer
=============

The node explorer provides views into a node's vault and transaction data using Corda's RPC framework.

When connected to an *Issuer* node, a user can execute cash transaction commands to issue and move cash to itself or other
parties on the network or to exit cash (for itself only).

When connected to a standard node a user can only execute cash transaction commands to move cash to other parties on the network.

.. note:: use the Explorer in conjunction with the Trader Demo and Bank of Corda samples to use existing *Issuer* nodes
          Alternatively run the demo nodes as described below which will launch two different *Issuer* nodes:
          a Royal Mint node and a Federal Reserve node.

Running the UI
--------------
**Windows**::

    gradlew.bat tools:explorer:run

**Other**::

    ./gradlew tools:explorer:run
    

Running demo nodes
------------------
**Windows**::

    gradlew.bat tools:explorer:runDemoNodes

**Other**::

    ./gradlew tools:explorer:runDemoNodes

.. note:: 5 Corda nodes will be created on the following port on localhost by default.

   * Notary -> 20002
   * Alice -> 20004
   * Bob -> 20006
   * Royal Mint -> 20008        (*Issuer node*)
   * Federal Reserve -> 20010   (*Issuer node*)

Interface
---------
Login
  User can login to any Corda node using the explorer. Alternatively, ``gradlew explorer:runDemoNodes`` can be used to start up demo nodes for testing.  
  Corda node address, username and password are required for login, the address is defaulted to localhost:0 if leave blank.
  Username and password can be configured via the ``rpcUsers`` field in node's configuration file; for demo nodes, it is defaulted to ``user1`` and ``test``.
  
.. note:: If you are connecting to the demo nodes, Alice, Bob, Royal Mint and Federal Reserve are all accessible using user1 credential, you won't be able to connect to the notary.

.. image:: resources/explorer/login.png
   :scale: 50 %
   :align: center
     
Dashboard
  The dashboard shows the top level state of node and vault.
  Currently, it shows your cash balance and the numbers of transaction executed.
  The dashboard is intended to house widgets from different CordApp's and provide useful information to system admin at a glance. 

.. image:: resources/explorer/dashboard.png
  
Cash
  The cash view shows all currencies you currently own in a tree table format, it is grouped by issuer -> currency.
  Individual cash transactions can be viewed by clicking on the table row. The user can also use the search field to narrow down the scope.

.. image:: resources/explorer/vault.png

New Transactions
  This is where you can create new cash transactions.
  The user can choose from three transaction types (issue, pay and exit) and any party visible on the network.

  General nodes can only execute pay commands to any other party on the network.

.. image:: resources/explorer/newTransactionCash.png

Issuer Nodes
  Issuer nodes can execute issue (to itself or to any other party), pay and exit transactions.
  The result of the transaction will be visible in the transaction screen when executed.

.. image:: resources/explorer/newTransactionIssuer.png

Transactions
  The transaction view contains all transactions handled by the node in a table view. It shows basic information on the table e.g. Transaction ID, 
  command type, USD equivalence value etc. User can expand the row by double clicking to view the inputs, 
  outputs and the signatures details for that transaction.  
  
.. image:: resources/explorer/transactionView.png

Network
  The network view shows the network information on the world map. Currently only the user's node is rendered on the map. 
  This will be extended to other peers in a future release.
  The map provides a intuitive way of visualizing the Corda network and the participants. 

.. image:: resources/explorer/network.png


Settings
  User can configure the client preference in this view.

.. note:: Although the reporting currency is configurable, FX conversion won't be applied to the values as we don't have an FX service yet.


.. image:: resources/explorer/settings.png
