TRUNCATE TABLE public.users CASCADE;

INSERT INTO public.users(id, created_date, modified_date, active, email, full_name, name, password, surname, username)
VALUES ('54ef5d94-e58c-4dd7-970a-366caf6080f3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true,
        'admin@admin.pl', 'Administrator', 'Administrator',
        '$2a$10$2c0JMBOE4q4XuuOtKWkO2.iDn/ruaDKAbTWQtrk3q5BdXUUfjRs0O', 'Administrator',
        'admin') ON CONFLICT DO NOTHING;
