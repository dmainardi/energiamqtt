/*
 * Copyright (C) 2024 Davide Mainardi <davide@mainardisoluzioni.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mainardisoluzioni.energiamqtt;

/**
 *
 * @author Davide Mainardi <davide@mainardisoluzioni.com>
 */
public class Main {
    public static void main(String[] args) {
        final String indirizzoBroker;
        
        if (args.length > 1) {
            indirizzoBroker = args[0];
            String operazione = args[1];    // può essere SEND oppure RECEIVE
            Energiamqtt instance = new Energiamqtt(indirizzoBroker, "test/topic");
            switch (operazione) {
                case "SEND":
                    instance.pubblica();
                    break;
                case "RECEIVE":
                    instance.sottoscrivitiEAspetta();
                    break;
                default:
                    System.err.println("Occhio che '" + operazione + "' non è un'operazione valida. Usare solo SEND oppure RECEIVE");
            }   
        }
        else
            System.err.println("È necessario indicare l'indirizzo IP del broker e il nome dell'operazione (SEND oppure RECEIVE)");
    }
}
