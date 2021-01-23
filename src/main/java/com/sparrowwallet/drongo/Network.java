package com.sparrowwallet.drongo;

public enum Network {
    MAINNET("mainnet", 36, "F", 5, "3", "grs", ExtendedKey.Header.xprv, ExtendedKey.Header.xpub, 1331),
    TESTNET("testnet", 111, "mn", 196, "2", "tgrs", ExtendedKey.Header.tprv, ExtendedKey.Header.tpub, 17777),
    REGTEST("regtest", 111, "mn", 239, "2", "grsrt", ExtendedKey.Header.tprv, ExtendedKey.Header.tpub, 18888);

    Network(String name, int p2pkhAddressHeader, String p2pkhAddressPrefix, int p2shAddressHeader, String p2shAddressPrefix, String bech32AddressHrp, ExtendedKey.Header xprvHeader, ExtendedKey.Header xpubHeader, int defaultPort) {
        this.name = name;
        this.p2pkhAddressHeader = p2pkhAddressHeader;
        this.p2pkhAddressPrefix = p2pkhAddressPrefix;
        this.p2shAddressHeader = p2shAddressHeader;
        this.p2shAddressPrefix = p2shAddressPrefix;
        this.bech32AddressHrp = bech32AddressHrp;
        this.xprvHeader = xprvHeader;
        this.xpubHeader = xpubHeader;
        this.defaultPort = defaultPort;
    }

    private final String name;
    private final int p2pkhAddressHeader;
    private final String p2pkhAddressPrefix;
    private final int p2shAddressHeader;
    private final String p2shAddressPrefix;
    private final String bech32AddressHrp;
    private final ExtendedKey.Header xprvHeader;
    private final ExtendedKey.Header xpubHeader;
    private final int defaultPort;

    private static Network currentNetwork;

    public String getName() {
        return name;
    }

    public int getP2PKHAddressHeader() {
        return p2pkhAddressHeader;
    }

    public int getP2SHAddressHeader() {
        return p2shAddressHeader;
    }

    public String getBech32AddressHRP() {
        return bech32AddressHrp;
    }

    public ExtendedKey.Header getXprvHeader() {
        return xprvHeader;
    }

    public ExtendedKey.Header getXpubHeader() {
        return xpubHeader;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public boolean hasP2PKHAddressPrefix(String address) {
        for(String prefix : p2pkhAddressPrefix.split("")) {
            if(address.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasP2SHAddressPrefix(String address) {
        return address.startsWith(p2shAddressPrefix);
    }

    public static Network get() {
        if(currentNetwork == null) {
            currentNetwork = MAINNET;
        }

        return currentNetwork;
    }

    public static void set(Network network) {
        if(currentNetwork != null && network != currentNetwork && !isTest()) {
            throw new IllegalStateException("Network already set to " + currentNetwork.getName());
        }

        currentNetwork = network;
    }

    private static boolean isTest() {
        return System.getProperty("org.gradle.test.worker") != null;
    }

    @Override
    public String toString() {
        return getName();
    }
}
