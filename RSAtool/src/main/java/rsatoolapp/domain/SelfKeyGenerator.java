package rsatoolapp.domain;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SelfKeyGenerator {
    public static void main(String[] args) {
        Random rnd1 = new Random(System.currentTimeMillis());
        Random rnd2 = new Random(System.currentTimeMillis() * 10);

        // valitaan kaksi suurta alkulukua p ja q niin, että p ≠ q
        // probablePrime metodin tuottama luku ei ole alkuluku
        // alle 2^(-100) todennäköisyydellä, eli 1 / 1 267 650 600 228 229 401 496 703 205 376
        // (1 suhde 1,27 kvintiljoonaan)
        BigInteger p = BigInteger.probablePrime(2048, rnd1);
        BigInteger q = BigInteger.probablePrime(2048, rnd2);

        // lasketaan modulus eli n = p * q
        BigInteger n = p.multiply(q);

        // määritellään ϕ(n)
        BigInteger pMinus1 = p.subtract(new BigInteger("1"));
        BigInteger qMinus1 = q.subtract(new BigInteger("1"));
        BigInteger phiN = pMinus1.multiply(qMinus1);

        // alustetaan e
        int pubKey = ThreadLocalRandom.current().nextInt(25, 100);

        // kasvatetaan e:n arvoa niin pitkään, että gcd(e, ϕ(n)) = 1
        // gcd = greatest common divisor = suurin yhteinen tekijä
        while (true) {
            BigInteger gcd = phiN.gcd(new BigInteger("" + pubKey));

            if (gcd.equals(BigInteger.ONE)) {
                break;
            }

            pubKey++;
        }

        // määritellään julkisen avaimen eksponentiksi e
        BigInteger e = new BigInteger("" + pubKey);

        // määritellään yksityisen avaimen eksponentiksi d
        BigInteger d = e.modInverse(phiN);

        System.out.println("p = " + p);
        System.out.println("q = " + q);

        System.out.println("Public key: " + e + ", " + n);
        System.out.println("Private key: " + d + ", " + n);
    }
}