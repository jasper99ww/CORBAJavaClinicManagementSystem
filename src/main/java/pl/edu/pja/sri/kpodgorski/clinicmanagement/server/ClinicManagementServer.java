package pl.edu.pja.sri.kpodgorski.clinicmanagement.server;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class ClinicManagementServer {
    public static void main(String[] args) {
        try {
            // Inicjalizacja ORB
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

            // Inicjalizacja POA (Portable Object Adapter) - adaptera obiektów Java do obiektów CORBA
            POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();

            // Inicjalizacja obiektu implementującego usługę
            ClinicManagementServant managementServant = new ClinicManagementServant();

            // Utworzenie referencji CORBA do obiektu implementującego usługę
            org.omg.CORBA.Object ref = poa.servant_to_reference(managementServant);

            // Zapisanie referencji do usługi nazwowej
            saveRefInNamingService(orb, ref, "Management");

            // Kod podtrzymujący działanie serwera w oczekiwaniu na wywołania klientów
            java.lang.Object sync = new java.lang.Object();
            synchronized (sync) {
                sync.wait();
            }
        } catch (Exception e) {
            System.out.println("Unexpected error in ClinicManagementServer: " + e.getMessage());
        }
    }

    /**
     * Metoda pomocnicza umożliwiająca zapisanie referencji obiektu CORBA do usługi nazwowej,
     * w celu jej późniejszego odczytu przez klienta usługi
     */

    private static void saveRefInNamingService(ORB orb, org.omg.CORBA.Object ref, String refName) throws Exception {
        // Uzyskanie referencji do usługi nazwowej
        org.omg.CORBA.Object o = orb.resolve_initial_references("NameService");

        // Rzutowanie obiektu CORBA (usługi nazwowej) na obiekt Java
        NamingContextExt rootContext = NamingContextExtHelper.narrow( o );

        // Tworzenie komponentu nazwowego
        NameComponent nc = new NameComponent(refName, "");

        // Tworzenie ścieżki nazwowej
        NameComponent[] path = {nc};

        // Rejestracja obiektu w usłudze nazwowej pod daną ścieżką
        rootContext.rebind(path, ref);
    }
}
