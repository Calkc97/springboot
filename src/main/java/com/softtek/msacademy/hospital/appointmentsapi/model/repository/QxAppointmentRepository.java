package com.softtek.msacademy.hospital.appointmentsapi.model.repository;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.QxAppointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QxAppointmentRepository extends CrudRepository<QxAppointment, Integer> {
    @Query(value = "SELECT * FROM qx_appointments WHERE id_qx = :id_qx",
            nativeQuery = true)
    List<QxAppointment> searchQxAppointmentsById(@Param("id_qx") int idQx);

    @Query(value = "SELECT * FROM qx_appointments WHERE id_qx = :id_qx AND (start_date = :date OR end_date = :date)",
            nativeQuery = true)
    List<QxAppointment> searchQxAppointmentsByIdAndDate(@Param("id_qx") int idQx, @Param("date") String date);

    @Query(value = "SELECT * FROM qx_appointments WHERE nss = :nss",
            nativeQuery = true)
    List<QxAppointment> searchPatientAppointmentsByNss(@Param("nss") String nss);

    @Query(value = "SELECT id_appointments, cancelled, start_date, end_date, start_hour, end_hour, assistance, nss, id_surgical_intervention_cat, id_qx  " +
            "FROM " +
            "   (SELECT id_appointments, cancelled, start_date, end_date, start_hour, end_hour, assistance, nss, id_surgical_intervention_cat, id_qx, " +
            "       (CAST(start_date AS DATETIME) + CAST(start_hour AS DATETIME)) as start_date_time, " +
            "       (CAST(end_date AS DATETIME) + CAST(end_hour AS DATETIME)) as end_date_time, " +
            "       (CAST(:start_date_new_appointment AS DATETIME) + CAST(CAST(:start_hour_new_appointment AS TIME) AS DATETIME)) as start_date_time_new_appointment, " +
            "       (CAST(:end_date_new_appointment AS DATETIME) + CAST(CAST(:end_hour_new_appointment AS TIME) AS DATETIME)) as end_date_time_new_appointment " +
            "    FROM qx_appointments " +
            "    WHERE id_qx = :id_qx AND cancelled = FALSE) AS qx_appointments_with_date_time " +
            "WHERE " +
            "   (start_date_time <= start_date_time_new_appointment AND end_date_time > start_date_time_new_appointment) " +
            "   OR (start_date_time < end_date_time_new_appointment AND end_date_time >= end_date_time_new_appointment) " +
            "   OR (start_date_time >= start_date_time_new_appointment AND end_date_time <= end_date_time_new_appointment) " +
            "LIMIT 1 ",
            nativeQuery = true)
    QxAppointment verifyQxAvailability(
            @Param("start_date_new_appointment") String startDateNewAppointment,
            @Param("start_hour_new_appointment") String startHourNewAppointment,
            @Param("end_date_new_appointment") String endDateNewAppointment,
            @Param("end_hour_new_appointment") String endHourNewAppointment,
            @Param("id_qx") int idQx);

    @Query(value = "SELECT id_appointments, cancelled, start_date, end_date, start_hour, end_hour, assistance, nss, id_surgical_intervention_cat, id_qx  " +
            "FROM " +
            "   (SELECT id_appointments, cancelled, start_date, end_date, start_hour, end_hour, assistance, nss, id_surgical_intervention_cat, id_qx, " +
            "       (CAST(start_date AS DATETIME) + CAST(start_hour AS DATETIME)) as start_date_time, " +
            "       (CAST(end_date AS DATETIME) + CAST(end_hour AS DATETIME)) as end_date_time, " +
            "       (CAST(:start_date_new_appointment AS DATETIME) + CAST(CAST(:start_hour_new_appointment AS TIME) AS DATETIME)) as start_date_time_new_appointment, " +
            "       (CAST(:end_date_new_appointment AS DATETIME) + CAST(CAST(:end_hour_new_appointment AS TIME) AS DATETIME)) as end_date_time_new_appointment " +
            "    FROM qx_appointments " +
            "    WHERE id_qx = :id_qx AND cancelled = FALSE AND id_appointments != :id_appointments) AS qx_appointments_with_date_time " +
            "WHERE " +
            "   (start_date_time <= start_date_time_new_appointment AND end_date_time > start_date_time_new_appointment) " +
            "   OR (start_date_time < end_date_time_new_appointment AND end_date_time >= end_date_time_new_appointment) " +
            "   OR (start_date_time >= start_date_time_new_appointment AND end_date_time <= end_date_time_new_appointment) " +
            "LIMIT 1 ",
            nativeQuery = true)
    QxAppointment verifyQxAvailabilityExcludingCurrentAppointment(
            @Param("start_date_new_appointment") String startDateNewAppointment,
            @Param("start_hour_new_appointment") String startHourNewAppointment,
            @Param("end_date_new_appointment") String endDateNewAppointment,
            @Param("end_hour_new_appointment") String endHourNewAppointment,
            @Param("id_qx") int idQx,
            @Param("id_appointments") int idAppointments);
}